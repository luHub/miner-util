package miner_pojos;

import java.nio.file.Path;

/**
 * Created by mey on 10/29/2016.
 */
public class CommitInfov2 {
    private Path filePath;
    private String hash;
    private String commitMessage;
    private String CommitBody;
	private String commiterName;
	private String commitDate;
	
    public CommitInfov2(String hash,String commitMessage,Path filePath,String commiterName, String date){
    	this.hash=hash;
        this.commitMessage=commitMessage;
        this.filePath=filePath;
        this.commiterName=commiterName;
        this.commitDate=date;
      
    }
    public CommitInfov2(String hash){
        this.hash=hash;
    }
    public CommitInfov2(String hash,String commitMessage,Path filePath,String commiterName){
        this.hash=hash;
        this.commitMessage=commitMessage;
        this.filePath=filePath;
        this.commiterName=commiterName;
    }

    public CommitInfov2() {
		// TODO Auto-generated constructor stub
	}
	public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getCommitBody() {
        return  CommitBody;
    }


    

    public Path getPath() {
    return filePath;
    }

	public String getFileName() {
		if (filePath != null && filePath.toString().length() > 0 && filePath.toString().contains("/")) {
			String[] fileName = filePath.toFile().toString().split("/");
			return fileName[fileName.length - 1];
		} else if (filePath != null && filePath.toString().length() > 0 && filePath.toString().contains("\\")) {
			String[] fileName = filePath.toFile().toString().split("\\\\");
			return fileName[fileName.length - 1];
		}
		return "";
	}
	
	public String getCommiterName() {
		return commiterName;
	}
	public void setCommiterName(String commiterName) {
		this.commiterName = commiterName;
	}
	public String getCommitDate() {
		return commitDate;
	}
	public void setCommitDate(String commitDate) {
		this.commitDate = commitDate;
	}
	public String getCommitMessage() {
		return commitMessage;
	}
	public void setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
	}
}
