package miner_pojos;

import java.util.List;

public class FileInfo {
    
	private String fileName;
    private String filePackage;
	private String authorName;
    private int minor;
    private int major;
	private int totalContributors;
    private double numberOfBugs;
	private double owner;
	private int totalLineContributors;
	private int lineContributorsMinor;
	private int lineContributorsMajor;
	private double lineContributorsOwnership;
	private double lineContributorsAuthor;
	private boolean lineContributoesAuthorOwner;


	public FileInfo(String fileName, String filePackage, String authorName,int minor, int major, int totalContributors, double numberOfBugs, double owner, int totalLineContributors, int lineContributorsMinor, int lineContributorsMajor, double lineContributorsOwnership, double lineContributorsAuthor, boolean lineContributoesAuthorOwner) {
		this.fileName = fileName;
		this.filePackage = filePackage;
		this.authorName = authorName;
		this.minor = minor;
		this.major = major;
		this.totalContributors = totalContributors;
		this.numberOfBugs = numberOfBugs;
		this.owner = owner;
		this.totalLineContributors = totalLineContributors;
		this.lineContributorsMinor = lineContributorsMinor;
		this.lineContributorsMajor = lineContributorsMajor;
		this.lineContributorsOwnership = lineContributorsOwnership;
		this.lineContributorsAuthor = lineContributorsAuthor;
		this.lineContributoesAuthorOwner = lineContributoesAuthorOwner;

	}

	public FileInfo(String fileName, String filePackage, Integer minor, Integer major, int totalContributors, int i, Double owner) {
		this.fileName = fileName;
		this.filePackage = filePackage;
		this.minor = minor;
		this.major = major;
		this.totalContributors = totalContributors;
		this.owner = owner;
	}


	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public double getLineContributorsOwnership() {
		return lineContributorsOwnership;
	}

	public void setLineContributorsOwnership(double lineContributorsOwnership) {
		this.lineContributorsOwnership = lineContributorsOwnership;
	}

	public String getFilePackage() {
		return filePackage;
	}

	public void setFilePackage(String filePackage) {
		this.filePackage = filePackage;
	}

	public int getTotalLineContributors() {
		return totalLineContributors;
	}

	public void setTotalLineContributors(int totalLineContributors) {
		this.totalLineContributors = totalLineContributors;
	}

	public int getLineContributorsMinor() {
		return lineContributorsMinor;
	}

	public void setLineContributorsMinor(int lineContributorsMinor) {
		this.lineContributorsMinor = lineContributorsMinor;
	}

	public int getLineContributorsMajor() {
		return lineContributorsMajor;
	}

	public void setLineContributorsMajor(int lineContributorsMajor) {
		this.lineContributorsMajor = lineContributorsMajor;
	}

	public double getLineContributorsAuthor() {
		return lineContributorsAuthor;
	}
	public void setLineContributorsAuthor(double lineContributorsAuthor) {
		this.lineContributorsAuthor = lineContributorsAuthor;
	}
	public boolean isLineContributoesAuthorOwner() {
		return lineContributoesAuthorOwner;
	}

	public void setLineContributoesAuthorOwner(boolean lineContributoesAuthorOwner) {
		this.lineContributoesAuthorOwner = lineContributoesAuthorOwner;
	}
	public boolean getLineContributorsAuthorOwner() {
		return this.lineContributoesAuthorOwner;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public double getMinor() {
		return minor;
	}
	public void setMinor(int minor) {
		this.minor = minor;
	}
	public double getMajor() {
		return major;
	}
	public void setMajor(int major) {
		this.major = major;
	}
	public double getTotalContributors() {
		return totalContributors;
	}
	public void setTotalContributors(int totalCommits) {
		this.totalContributors = totalCommits;
	}
	public double getNumberOfBugs() {
		return numberOfBugs;
	}
	public void setNumberOfBugs(double numberOfBugs) {
		this.numberOfBugs = numberOfBugs;
	}
	public double getOwner() {
		return owner;
	}
	public void setOwner(double owner) {
		this.owner = owner;
	}




	@Override
	public String toString(){
		return fileName+","+filePackage+","+minor+","+major+","+totalContributors+","+owner+","+numberOfBugs;
	}
}
