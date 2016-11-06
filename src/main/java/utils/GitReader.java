package utils;

import miner_pojos.CommitInfo;
import miner_pojos.CommitInfov2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import core.Config;


public class GitReader {




	//This is map that will be running in 2 threads so caution
	
	
	//From Java World
	class StreamGobbler extends Thread
	{
	    InputStream is;
	    String type;
		private Map<String, Integer> devInformationMap;
	   
	   
	    StreamGobbler(InputStream is, String type)
	    {
	        this.is = is;
	        this.type = type;
	      
	    }
	    
		public void run() {
			try {

				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				System.out.println("Analizing file Commits:" +type);
				System.out.println(line);
				while ((line = br.readLine()) != null) {
					if (type.equals("OUTPUT")) {
						//TODO Change 
						System.out.println("OUTPUTLINE: ");
						if (!devInformationMap.containsKey(line)) {
							devInformationMap.put(line, 1);
						} else {
							Integer commitCount = devInformationMap.get(line);
							devInformationMap.replace(line, commitCount + 1);
						}
					}
				}

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	


	/**
	 * Returns all the .java file paths from a remote git repository
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	
	public static List<Path> readGitJavaPathsWithBlackList(Path gitProjectPath,String blackPaths) throws IOException, InterruptedException{
		
		List<Path> listOfPaths = new ArrayList<Path>();
		String[] command = {"CMD", "/C", "git ls-files"};
	    ProcessBuilder probuilder = new ProcessBuilder(command);
	    probuilder.directory(gitProjectPath.toFile());
	    Process process;
            process = probuilder.start();
            //Read out dir output
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
            	if(PathFilters.checkPathFilterEnding(line,"java")&& !line.contains(blackPaths) ){
					Path path = Paths.get(gitProjectPath+"\\"+line);
					listOfPaths.add(path);
                }
            }
            int exitValue = process.waitFor();
            //Close InputStream
            br.close();
            isr.close();
            process.destroy();
            System.out.println("End javaFile List");
	    return listOfPaths ;
	    }
	
	public static List<Path> readGitJavaPaths(Path gitProjectPath) throws IOException, InterruptedException{
		List<Path> listOfPaths = new ArrayList<Path>();
		String[] command = {"CMD", "/C", "git ls-files"};
	    ProcessBuilder probuilder = new ProcessBuilder(command);
	    probuilder.directory(gitProjectPath.toFile());
	    Process process;
            process = probuilder.start();
            //Read out dir output
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
            	if(PathFilters.checkPathFilterEnding(line,"java")){
					Path path = Paths.get(gitProjectPath+"\\"+line);
					listOfPaths.add(path);
                	//TODO  Add Logger
                }
            }
            int exitValue = process.waitFor();
            //Close InputStream
            br.close();
            isr.close();
            process.destroy();
            System.out.println("End javaFile List");
	    return listOfPaths ;
	    }
	    
	    
	
	
	
	
	/**
	 * Read from Git the using the shortlog command for a desired repository,
	 * directly to a cache.
	 * @param until
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public Map<String, Integer> readFileCommitsFromDevelopers(String gitProjectPath,String path, String since, String until) throws IOException, InterruptedException {
		
		Map<String, Integer> devInformationMap = new HashMap<String, Integer>();
		
		String[] command ={"CMD", "/C", "git log --pretty=format:\"%an\" --since=" +"\""+since+" \""+" --until="+"\""+until+"\" -- " + path}; // "+ "--until=" +"\""+ until +"\""+ " -p " + path});// shortlog -s"}
	    ProcessBuilder probuilder = new ProcessBuilder(command);
	    probuilder.directory(new File(gitProjectPath));
	    Process process;
            process = probuilder.start();
            //Read out dir output
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
            	if (!devInformationMap.containsKey(line)) {
            		devInformationMap.put(line, 1);
				} else {
					Integer commitCount = devInformationMap.get(line);
					devInformationMap.replace(line, commitCount + 1);
				}
            }
            int exitValue = process.waitFor();
            //Close InputStream
            br.close();
            isr.close();
            process.destroy();
		
		/*
		 try
	        {            
	             Runtime rt = Runtime.getRuntime();
	            Process proc = rt.exec(new String[]{"CMD", "/C","git","log","--pretty=format:\"%an\"","--since=" +"\""+since+" \""+"--until="+"\""+until+"\""+" -p " + path}); // "+ "--until=" +"\""+ until +"\""+ " -p " + path});// shortlog -s"});
	            // any error message?
	            StreamGobbler errorGobbler = new 
	                StreamGobbler(proc.getErrorStream(), "ERROR");            
	            
	            // any output?
	            StreamGobbler outputGobbler = new 
	                StreamGobbler(proc.getInputStream(), "OUTPUT");
	                
	            // kick them off
	            errorGobbler.start();
	            outputGobbler.start();
	                                  
	            outputGobbler.join();
	            // any error???
	            int exitVal = proc.waitFor();
	            //TODO Log Errors
	        } catch (Throwable t)
	          {
	            t.printStackTrace();
	          }
		*/
		

		
		
		
		/*ProcessBuilder probuilder = new ProcessBuilder(command);
		probuilder.directory(new File(pathProject));
		

			Process process = probuilder.start();
			probuilder.redirectErrorStream(true);
			
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
		
			String line; 
			while ((line = br.readLine()) != null ) {
				// Parse the response in Number of Commits and Developers
				String[] tokens = line.split("\\t+");
				devInformationMap.put(tokens[0], tokens[1]);
			}
			br.close();
			isr.close();*/
		
		return devInformationMap;
	}


	/**
	 * Git Commit List from each File
	 */
	public static LinkedList<CommitInfov2> getCommitsFromFile(Path projectPath, Path path) throws IOException, InterruptedException {
        final int HASH_CODE = 0;
        final int COMMITER = 1;
        final int COMMIT_DATE=2;
        final int COMMIT_MESSAGE=3;
        
        String since = Config.getInstace().getInitDate();
        String until = Config.getInstace().getFinalDate();
        
		LinkedList<CommitInfov2> commitsList = new LinkedList<>();
		String format = "%H,,,%an,,,%cd,,,%B:::";
		String[] command ={"CMD", "/C", "git log --pretty="+format+" "+"--since=" +"\""+since+" \""+" --until="+"\""+until+"\" -- "+ path.toString().replaceAll("\\\\","/")};
		ProcessBuilder probuilder = new ProcessBuilder(command);
		probuilder.directory(new File(projectPath.toString()));
		Process process;
		process = probuilder.start();
		//Read out dir output
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		StringBuffer srtbuilder = new StringBuffer();
		while ((line = br.readLine()) != null) {
		srtbuilder.append(line);
		}
		
		process.waitFor();
		//Close InputStream
		br.close();
		isr.close();
		process.destroy();
		
		if (srtbuilder.length() > 0) {
			String[] splitCommits = srtbuilder.toString().split(":::");
			for (String commit : splitCommits) {
				String[] splitLine = commit.toString().split(",,,");
				commitsList.add(new CommitInfov2(splitLine[HASH_CODE], splitLine[COMMIT_MESSAGE].replace(":", ""), path,
						splitLine[COMMITER], splitLine[COMMIT_DATE]));
			}
		}
		
		
		
        return  commitsList;
	}

	public static String gitGetFileVersion(Path projectPath, CommitInfov2 commitInfov2)  throws IOException, InterruptedException  {
		//Relative Path
		String relative = projectPath.toFile().toURI().relativize(commitInfov2.getPath().toFile().toURI()).getPath();
        String[] command ={"CMD", "/C", "git show "+ commitInfov2.getHash()+":./"+ relative.replaceAll("\\\\","/")};
        ProcessBuilder probuilder = new ProcessBuilder(command);
        probuilder.directory(new File(projectPath.toString()));
        Process process;
        process = probuilder.start();
        //Read out dir output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer strBuffer = new StringBuffer();
		String line;
        while ((line = br.readLine()) != null) {
            strBuffer.append(line.toString());
        	strBuffer.append("\n");
		}
        int exitValue = process.waitFor();
        //Close InputStream
        br.close();
        isr.close();
        process.destroy();
        return  strBuffer.toString();
    }
}