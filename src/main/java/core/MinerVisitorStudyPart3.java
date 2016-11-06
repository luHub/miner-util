package core;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.domain.Modification;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.BlamedLine;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.SCMRepository;
import miner_pojos.DiffInfo;
import utils.GitInfo;
import utils.JiraExplore;
import utils.JiraReader;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by LuHub on 9/26/2016.
 */
public class MinerVisitorStudyPart3 implements CommitVisitor {

    private String specificPath="";

    public MinerVisitorStudyPart3(String specificPath){
        this.specificPath=specificPath;
    }

    private JiraExplore jiraExplore = new JiraExplore();
    private static final String[] keywordksList = {"fix","error", "bug", "fix", "issue", "mistake", "incorrect", "fault", "defect", "flaw","typo"};
    private List<InductedBugMetrics> inductedBugMetricsList = new ArrayList<>();

    @Override
    public void process(SCMRepository repo, Commit commitFix, PersistenceMechanism writer) {
        //Look for commits that fixed a bug
        if(commitFix.getBranches().contains("master")) {
            String commitMessage = commitFix.getMsg();
            for (Modification m : commitFix.getModifications()) {
                if (m.getFileName().contains(specificPath)) {
                    String issueId = jiraExplore.readIssueId(commitMessage,Pattern.compile("Lucene-[0-9]+"));
                    if (jiraExplore.isIssue(commitMessage,Pattern.compile("Lucene-[0-9]+")) && JiraReader.IsBug(issueId,"https://issues.apache.org/jira/rest/api/2/issue/")) {
                        InductedBugMetrics inductedBugMetrics = populateInducedMetric(repo, commitFix, m);
                        this.inductedBugMetricsList.add(inductedBugMetrics);
                    } else if (hasKeywords(keywordksList, commitMessage)) {
                        InductedBugMetrics inductedBugMetrics = populateInducedMetric(repo, commitFix, m);
                        this.inductedBugMetricsList.add(inductedBugMetrics);
                    }
                }
            }
        }
    }

    private InductedBugMetrics populateInducedMetric(SCMRepository repo, Commit commit,Modification m) {
        InductedBugMetrics inductedBugMetrics = calculateBugsInduced(repo,commit,m);
        inductedBugMetrics.incrementPostReleaseBug();
        inductedBugMetrics.setFixCommitHash(commit.getHash());
        inductedBugMetrics.setFixCommitTimeStamp(commit.getDate());
        return inductedBugMetrics;
    }

    @Override
    public String name() {
        return null;
    }


    //FileName
    //Commit Hash
    //BugInduced
    //Note: Here is where to put the Algorithm to check induced bugs, current hypothesis is that
    //lines removed in fix are bugs.
    private  InductedBugMetrics calculateBugsInduced(SCMRepository repo, Commit commitFix,Modification m) {
        InductedBugMetrics inducedBugMetric = new InductedBugMetrics();
        boolean end=false;
        List<BlamedLine> blamedList = new ArrayList();
        List<DiffInfo> diffInfoList = new ArrayList();
                blamedList = repo.getScm().blame(m.getFileName(), commitFix.getHash(), true);
            diffInfoList = GitInfo.extractFromGitDiffModifiedLines(m.getDiff()); //@@ -567,8 +567,8 -567,8 @@
            for(BlamedLine blamedLine : blamedList){
                //Where the Commiter Commits its Commit
                int blamedLineNumber = blamedLine.getLineNumber();
                for(DiffInfo diffInfo : diffInfoList){
                    Integer diffLineNumber = diffInfo.getLineNumber();
                    Integer diffLineModification = diffInfo.getLinesModifications();
                    Integer totalModif = Math.abs(diffLineModification)+diffLineNumber;
                    boolean isRemovedLines = diffLineModification<0;
                    if(isRemovedLines && blamedLineNumber>=diffLineNumber && (blamedLineNumber <= totalModif)){
                        PairCommitFile pairCommitFile = new PairCommitFile(blamedLine.getCommit(),m.getFileName());
                        if(!inducedBugMetric.getBugCommitFileNameMap().containsKey(pairCommitFile)){
                            inducedBugMetric.getBugCommitFileNameMap().put(pairCommitFile,1);
                            break;
                        }

                    }
                }
        }
        return inducedBugMetric;
    }

    public List<InductedBugMetrics> getInductedBugMetricsList() {
        return inductedBugMetricsList;
    }


  public  class PairCommitFile{

        public PairCommitFile(String commit, String fileName) {
            this.commitHash=commit;
            this.fileName=fileName;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((commitHash == null) ? 0 : commitHash.hashCode());
            result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PairCommitFile other = (PairCommitFile) obj;
            if (commitHash == null) {
                if (other.commitHash != null)
                    return false;
            } else if (!commitHash.equals(other.commitHash))
                return false;
            if (fileName == null) {
                if (other.fileName != null)
                    return false;
            } else if (!fileName.equals(other.fileName))
                return false;
            return true;
        }

        private String commitHash;
        private String fileName;


        public String getCommitHash() {
            return commitHash;
        }

        public void setCommitHash(String commitHash) {
            this.commitHash = commitHash;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
    /**
     * Induced Bugs Class
     */
    public class InductedBugMetrics {


        private Map<PairCommitFile,Integer> bugCommitFileNameMap=new HashMap<>();
        private int postReleaseBug;
        private int devTimeBug;
        private String fixCommitHash;
        private Calendar fixCommitTimeStamp;


        public int getPostReleaseBug() {
            return postReleaseBug;
        }

        public void incrementPostReleaseBug() {

            this.postReleaseBug++;
        }

        public void incrementDevTimeBugs() {
            this.devTimeBug++;
        }

        public int getDevTimeBug() {
            return devTimeBug;
        }

        public String getFixCommitHash() {
            return fixCommitHash;
        }

        public void setFixCommitHash(String fixCommitHash) {
            this.fixCommitHash = fixCommitHash;
        }

        public Calendar getFixCommitTimeStamp() {
            return fixCommitTimeStamp;
        }

        public void setFixCommitTimeStamp(Calendar fixCommitTimeStamp) {
            this.fixCommitTimeStamp = fixCommitTimeStamp;
        }

        public Map<PairCommitFile, Integer> getBugCommitFileNameMap() {
            return bugCommitFileNameMap;
        }

        public void setBugCommitFileNameMap(Map<PairCommitFile, Integer> bugCommitFileNameMap) {
            this.bugCommitFileNameMap = bugCommitFileNameMap;
        }

        public void setPostReleaseBug(int postReleaseBug) {
            this.postReleaseBug = postReleaseBug;
        }
    }

    private boolean hasKeywords(String[] keywords, String message){
        for(String bugKey :keywords){
            if(message.contains(bugKey)){return true;}
        }
        return false;
    }
}