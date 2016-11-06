package core;

import miner_pojos.ChangeMetric;
import miner_pojos.CommitInfov2;
import utils.GitReader;
import utils.JiraExplore;
import utils.JiraReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import junit.framework.Assert;

/**
 * Created by mey on 10/29/2016.
 */
public class MethodBugLevelCollector {

	private int counter;
	
    final static Logger logger = Logger.getLogger(MethodBugLevelCollector.class);
    private Config config = Config.getInstace();
    private Path gitProjectPath;
    private String toDate;
    private String fromDate;
    private Pattern issuePattern;
    private Pattern jiraId;
    private String jiraUrl;
    private Map<Key,ChangeMetric> changeMetrics = new LinkedHashMap<Key, ChangeMetric>();

    public MethodBugLevelCollector(){
        this.gitProjectPath = Paths.get(config.getProjectPath());
        this.fromDate=config.getInitDate();
        this.toDate=config.getFinalDate();
        this.issuePattern=Pattern.compile(config.getJiraIssuePattern());
        this.jiraId=Pattern.compile(config.getJiraId());
        this.jiraUrl=config.getJiraUrl();
    }

    public void populateMetrics() throws IOException, InterruptedException {
            List<Path> javaFilesPaths = new ArrayList<>();
            String blackListPath = config.getBlackListPath();
            javaFilesPaths.addAll(GitReader.readGitJavaPathsWithBlackList(this.gitProjectPath, blackListPath));
            
            javaFilesPaths.stream().forEach((path)->{
              //2) Get Commit History For Java File
                final LinkedList<CommitInfov2> commitListHisotry;
                try {
                	//Reverser Commits to iterate from Start to Finish
                    commitListHisotry = findCommitHistory(this.fromDate,this.toDate,path);
                    Collections.reverse(commitListHisotry);

                   //3) Find which commit Fixs a Bug, isBug goes to Jira and with Finds it out using Commit Id and searching for Issue Type
                  commitListHisotry.stream().forEach((commit)->{
                  //4) Check Previous File Version and Discover if it was a bug Fix in Method Level Comparing FixCommit File and Previous Commit.
                  compareFileVersions(path, commitListHisotry, commit);
              });
            } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        });
            //5 End Distilling
		System.out.println("Program Finished: Bugs Number: "
				+ this.changeMetrics.entrySet().stream().mapToInt((k) -> k.getValue().getBugs()).sum());
	}

	private void compareFileVersions(Path path, final LinkedList<CommitInfov2> commitListHisotry,
			CommitInfov2 commit) {
		ListIterator<CommitInfov2> iterator = commitListHisotry.listIterator(commitListHisotry.indexOf(commit));
		// TODO check if previous is working good
		if (iterator.hasPrevious()) {
			try {
				//PrepareFilesHelpers
				String file = gitGetFile(commit);
				FileInfoHelper fileInfo = new FileInfoHelper(commit, file,isBugFix(commit));
				CommitInfov2 previousCommit = iterator.previous();
				String filePrevious = gitGetFile(previousCommit);
				FileInfoHelper filePreviousInfo = new FileInfoHelper(previousCommit, filePrevious);
				//Call ChangeDistiller
				changeDistiller(fileInfo, filePreviousInfo);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.print("File in Path: " + path + " have a fix but not a previous commit");
		}
	}

    private void changeDistiller(FileInfoHelper fileInfo, FileInfoHelper filePreviousInfo) throws IOException {
		//Prepare Files Version 
    	String filePath =Config.getInstace().getJavaV2Path();
		File file = createFile(fileInfo,filePath);

		String previousFilePath =Config.getInstace().getJavaV1Path();
		File fileprevious = createFile(filePreviousInfo,previousFilePath); 
		
		System.out.println("Analizing File: "+fileInfo.getCommitInfo().getFileName()+" VS "+filePreviousInfo.getCommitInfo().getFileName());
		System.out.println("Commit Hash: "+fileInfo.getCommitInfo().getHash()+" VS "+filePreviousInfo.getCommitInfo().getHash());
		System.out.println("Commit Date: "+fileInfo.getCommitInfo().getCommitDate());
		
		//Call Change Distiller
		List<SourceCodeChange> changes = runChangeDistiller(file, fileprevious);
        updateChangeMetrics(fileInfo,changes);
        System.out.println("End Analyzing");
        System.out.println("Bugs Number: " + this.changeMetrics.entrySet().stream().mapToInt((k) -> k.getValue().getBugs()).sum());
        System.out.println("*************");
    } 

	private void updateChangeMetrics(FileInfoHelper fileInfo, List<SourceCodeChange> changes) {
		// Key is the Complete Method Path from Change Distiller
		final List<UpdatesForCommits> updatesForCommitsList = new ArrayList<>();
		counter++;
		if (changes != null) {
			//TODO Filter properly for case of parent method and child method
			changes.stream().filter((change) ->  change.getRootEntity().getType().equals(JavaEntityType.METHOD)
					|| change.getParentEntity().getType().equals(JavaEntityType.METHOD)
	   		        || change.getChangedEntity().getType().equals(JavaEntityType.METHOD)
	   		        && !change.getRootEntity().getType().equals(JavaEntityType.JAVADOC)
	   		        && !change.getChangedEntity().getType().equals(JavaEntityType.JAVADOC)
	   		        && !change.getParentEntity().getType().equals(JavaEntityType.JAVADOC)
					)		
			.forEach((methodChange) -> {
						Key mapKey = null;
						if(methodChange.getRootEntity().getType().equals(JavaEntityType.METHOD)){
							
							 mapKey = new Key(methodChange.getRootEntity().getUniqueName());
							if (methodChange.getChangeType().equals(ChangeType.METHOD_RENAMING)
											&& methodChange instanceof Update) {
								mapKey = updateKeyInMethodStatementChange(methodChange, this.changeMetrics,
										updatesForCommitsList);
							}
							 addMethodToMap(methodChange,mapKey,methodChange.getRootEntity().getUniqueName());
						}else if(methodChange.getParentEntity().getType().equals(JavaEntityType.METHOD)){
							 mapKey = new Key(methodChange.getParentEntity().getUniqueName());
							if (methodChange.getChangeType().equals(ChangeType.METHOD_RENAMING)
											&& methodChange instanceof Update) {
								mapKey = updateKeyInMethodStatementChange(methodChange, this.changeMetrics,
										updatesForCommitsList);
							}
							 addMethodToMap(methodChange,mapKey,methodChange.getParentEntity().getUniqueName());
						}else if(methodChange.getChangedEntity().getType().equals(JavaEntityType.METHOD)){
							 mapKey = new Key(methodChange.getChangedEntity().getUniqueName());
							if (methodChange.getChangeType().equals(ChangeType.METHOD_RENAMING)
											&& methodChange instanceof Update) {
								mapKey = updateKeyInMethodStatementChange(methodChange, this.changeMetrics,
										updatesForCommitsList);
							}
							 addMethodToMap(methodChange,mapKey,methodChange.getParentEntity().getUniqueName());
						}
						this.changeMetrics.get(mapKey)
								.updateMetricsPerChange(fileInfo, methodChange);
						//Changes per Commit
						Key mk = new Key(mapKey);
						Optional<UpdatesForCommits> opt = updatesForCommitsList.stream()
								.filter(p -> p.getMethodName().equals(mk)).findFirst();
						if (opt.isPresent()) {
							opt.get().update(methodChange);
						} else {
							UpdatesForCommits updatesForCommits = new UpdatesForCommits();
							updatesForCommits.setMethodName(mapKey);
							updatesForCommits.update(methodChange);
							updatesForCommitsList.add(updatesForCommits);
						}
					});
 
			updatesForCommitsList.stream().filter(ufc -> this.changeMetrics.containsKey(ufc.getMethodName())).forEach(p->{
				this.changeMetrics.get(p.getMethodName()).updateMetricsPerCommit(fileInfo,p);
				//Confirm where to add fix bug to which change method
				if (fileInfo.commitFixBug) {
					this.changeMetrics.get(p.getMethodName()).updateNumberOfBugs();
				}
			});
			
			if (counter > 1) {
				// To see what the table looks like print it while doing
				try {
					createFile("results.csv", this.changeMetrics);
					counter=0;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	private Key updateKeyInMethodStatementChange(SourceCodeChange methodChange, Map<Key, ChangeMetric> changeMetrics,
			List<UpdatesForCommits> updatesForCommitsList) {
		final Key newMapKey = new Key(((Update) methodChange).getNewEntity().getUniqueName());
		final Key oldMapKey = new Key(((Update) methodChange).getChangedEntity().getUniqueName());

		if (changeMetrics.containsKey(oldMapKey)) {
			ChangeMetric cm = this.changeMetrics.get(oldMapKey);
			this.changeMetrics.remove(oldMapKey);
			this.changeMetrics.put(newMapKey, cm);
		}
		for (UpdatesForCommits upc : updatesForCommitsList) {
			if (upc.getMethodName().equals(oldMapKey)) {
				upc.setMethodName(newMapKey);
			}
		}
		return newMapKey;
	}

	private void createFile(String fileName, Map<Key, ChangeMetric> changeMetrics) throws IOException {
		createPathIfNotExist(config.getTablePath());
		Path path = Paths.get(config.getTablePath()+"\\"+fileName);
		if (Files.exists(path)) {
			Files.delete(path);
		}
		Files.createFile(path);
		Charset charset = Charset.forName("US-ASCII");
		BufferedWriter writer = Files.newBufferedWriter(path, charset);
		
		String dataInfo="From: "+config.getInitDate()+"\t To: "+config.getFinalDate();
		writer.write(dataInfo+" \n");
		String titles = "methodName,"+"methodHistories," + "authors," + "sumOfStmtAdded," + "maxStmtAdded," + "avgStmtAdded,"
				+ "sumOfStmtDeleted," + "maxStmtDeleted," + "avgStmtDeleted," + "churn," + "maxChurn," + "avgChurn,"
				+ "decl," + "cond," + "elseAdded," + "elseDeleted," + "numberOfBugs\n";
		writer.write(titles+" \n");
		 changeMetrics.entrySet();
		 
		 changeMetrics.entrySet().stream().forEach((m)->{
			 try {
				writer.write(m.getKey().toString().replaceAll(",", "::") +","+ m.getValue().toString() + "\n");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				 
		 });
		 /*
		Iterator it = changeMetrics.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			writer.write(pair.getKey().toString() +","+ pair.getValue().toString() + "\n");
			it.remove(); //DANGER!! // avoids a ConcurrentModificationException
		}*/
		writer.flush();
		writer.close();
	}

	private void addMethodToMap(SourceCodeChange methodChange,Key key,String uniqueName) {
		ChangeMetric changeMetric = new ChangeMetric(uniqueName); 
		if (!this.changeMetrics.containsKey(key)) {
			//System.out.println("Change Entity Parent: "+methodChange.getParentEntity().getType());
			//System.out.println("Changed Entity: "+methodChange.getChangedEntity().getUniqueName());
			//System.out.println("Changed Entity Type: "+ methodChange.getChangedEntity().getType());
			
			this.changeMetrics.put(key, changeMetric);
		}
		//TODO Check for Renames and Create and Create a join of Renames to later merger or 
		//something like that
	}

	private List<SourceCodeChange> runChangeDistiller(File file, File fileprevious) {
		FileDistiller distiller = ChangeDistiller.createFileDistiller(ChangeDistiller.Language.JAVA);
        try {
            distiller.extractClassifiedSourceCodeChanges(fileprevious, file);
        } catch (Exception e) {
			/*
			 * An exception most likely indicates a bug in ChangeDistiller.
			 * Please file a bug report at
			 * https://bitbucket.org/sealuzh/tools-changedistiller/issues and
			 * attach the full stack trace along with the two files that you
			 * tried to distill.
			 */
            System.err.println("Warning: error while change distilling. " + e.getMessage());
        }
        return distiller.getSourceCodeChanges();

	}

	private String gitGetFile(CommitInfov2 commitInfov2) throws IOException, InterruptedException {
         return GitReader.gitGetFileVersion(this.gitProjectPath,commitInfov2);
    }

    private boolean isBugFix(CommitInfov2 commitInfo) {
        String jiraId = extractJiraId(commitInfo.getCommitMessage());
        String commitMessage = commitInfo.getCommitMessage();
        logger.debug("IsBug: "+JiraExplore.isIssue(commitMessage,this.issuePattern));
        System.out.println("Jira Message: "+ jiraId  +" IsBug: "+JiraExplore.isIssue(jiraId,this.issuePattern));
        
        //First check if is an issue in jira then in the commit message
        return (!jiraId.isEmpty() && JiraReader.IsBug(jiraId,jiraUrl)) || JiraExplore.isIssue(commitMessage,this.issuePattern) ;
    }

	private String extractJiraId(String commitMessage) {
		String x = "";
		Matcher m = this.jiraId.matcher(commitMessage);
		if (m.find()) {
			x = m.group();
		}
		return x;
	}

	private LinkedList<CommitInfov2> findCommitHistory(String fromDate, String toDate, Path javafilePath) throws IOException, InterruptedException {
            return GitReader.getCommitsFromFile(this.gitProjectPath,javafilePath);
    }

    /**
     * TODO Change ChangeDistiller Source Code to Accept Strings instead of files
     * File IO Part: 
     * 
     */
    	private static File createFile(FileInfoHelper fileInfoHelper,String directory) throws IOException {
    		createPathIfNotExist(directory);
    		Path path = Paths.get(directory + "\\" + fileInfoHelper.getCommitInfo().getFileName());
    		if (Files.exists(path)) {
    			Files.delete(path);
    		}
    		Path filePath = Files.createFile(path);
    		Charset charset = Charset.forName("US-ASCII");
    		BufferedWriter writer = Files.newBufferedWriter(path, charset);
    		writer.write(fileInfoHelper.getFileBody(), 0, fileInfoHelper.getFileBody().length());
    		writer.flush();
    		writer.close();
    		return filePath.toFile();
    	}
    	
    	public static void createPathIfNotExist(String directory) throws IOException {
    		// Check if directory exists
    		Path path = Paths.get(directory);
    		if (!Files.exists(path)) {
    			// Create Directory
    			Files.createDirectories(path);
    		}
    	}
    	
    	/**
    	 * 
    	 * @author mey
    	 * Helper Class to compare versions
    	 * This file Scope is only to do Change Distiller Comparison and update final list
    	 */
    	public class FileInfoHelper{
    		
    		private CommitInfov2 commitInfo;
    		private Boolean commitFixBug;
    		private String fileBody;
    		
    		public FileInfoHelper(CommitInfov2 commitInfo, String fileBody){
    			this.commitInfo=commitInfo;
    			this.fileBody=fileBody;
    		}
    		
    		public FileInfoHelper(CommitInfov2 commitInfo, String fileBody, Boolean commitFixBug){
    			this.commitInfo=commitInfo;
    			this.fileBody=fileBody;
    			this.commitFixBug=commitFixBug;
    		}

			public FileInfoHelper() {
				// TODO Auto-generated constructor stub
			}

			public CommitInfov2 getCommitInfo() {
				return commitInfo;
			}

			public void setCommitInfo(CommitInfov2 commitInfo) {
				this.commitInfo = commitInfo;
			}

			public String getFileBody() {
				return fileBody;
			}

			public void setFileBody(String fileBody) {
				this.fileBody = fileBody;
			}

			public Boolean getCommitFixBug() {
				return commitFixBug;
			}

			public void setCommitFixBug(Boolean commitFixBug) {
				this.commitFixBug = commitFixBug;
			}
    		
    	}
    	
    	
    	public class UpdatesForCommits{
    		//TODO Create a list of Methods Names
    		private Key methodName;
    		// Maximum number of source code statements added to a method body for
    		// all method histories
    		private int maxCodeStatementsAdded;
    		// Maximum number of source code statements deleted from a method body
    		// for all method histories
    		private int maxMethodStatementsDeleted;

    		// Maximum churn for all method histories
    		private int maximunChurn;
    		
    		public void increaseMaxCodeStatementsAdded(){
    			maxCodeStatementsAdded++;
    			updateMaxChurn();
    		}
    		
    		public void setMethodName(Key key) {
				// TODO Auto-generated method stub
				this.methodName=key;
			}

			public void update(SourceCodeChange methodChange) {
			
    			if (methodChange.getChangeType().equals(ChangeType.STATEMENT_INSERT)) {
    				// Sum of all source code statements added to a method body over all
    				// method histories
    				this.maxCodeStatementsAdded++;
    				// churn: Sum of stmtAdded - stmtDeleted over all method histories
    				updateMaxChurn();
    			}
    			if (methodChange.getChangeType().equals(ChangeType.STATEMENT_DELETE)) {
    				// Sum of all source code statements deleted from a method body over
    				// all method histories
    				this.maxMethodStatementsDeleted++;
    				// churn: Sum of stmtAdded - stmtDeleted over all method histories
    				updateMaxChurn();
    			}
    			
			}

			public void increaseMaxCodeStatementsDeleted(){
    			maxMethodStatementsDeleted++;
    			updateMaxChurn();
    		}
    		
			private  void updateMaxChurn(){
				this.maximunChurn=maxCodeStatementsAdded-maxMethodStatementsDeleted;
			}


			public Integer getMaxCodeStatements() {
				return maxCodeStatementsAdded;
			}
			


			public Integer getMaximunMethodStatementsDeleted() {
				return maxMethodStatementsDeleted;
			}


			public Integer getMaximunChurn() {
				return maximunChurn;
			}

			public Key getMethodName() {
				return methodName;
			}

			
    	}
    	
    	public class Key{
    		
    		@Override
    		public String toString(){
    			return methodName;
    		}
    		
    		@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
				return result;
			}
			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (!(obj instanceof Key))
					return false;
				Key other = (Key) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (methodName == null) {
					if (other.methodName != null)
						return false;
				} else if (!methodName.equals(other.methodName))
					return false;
				return true;
			}
			String methodName;
    		public Key(String key){
    			this.methodName=key.split("\\(")[0]; 
    		}
    		
    		public Key(Key key){
    			this.methodName=key.methodName;
    		}
    		
			private MethodBugLevelCollector getOuterType() {
				return MethodBugLevelCollector.this;
			}
    	}
}