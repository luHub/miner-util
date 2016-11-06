package core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mey on 9/26/2016.
 */
public class Config {
    //TODO create later a config file .config to read the setup of this project
    private static Config config;
    private String initDate;
    private String finalDate;
    private String projectPath;
    private String jiraIssuePattern;
    private String jiraUrl;
    private String javaV1Path;
    private String javaV2Path;
    private String tablePath;
    private String blackListPath;
	private String jiraId;



    public String getInitDate() {
        return initDate;
    }
    public String getFinalDate() {
        return finalDate;
    }

    private Config(){
        Properties studyProperties = new Properties();
        InputStream input  = getClass().getClassLoader().getResourceAsStream("config.properties");
        try {
			studyProperties.load(input);
			this.initDate = studyProperties.getProperty("initDate");
			this.finalDate = studyProperties.getProperty("finalDate");
			this.projectPath = studyProperties.getProperty("projectPath");
			this.jiraIssuePattern = studyProperties.getProperty("commitMessagePattern");
			this.jiraId = studyProperties.getProperty("jiraId");
			this.jiraUrl = studyProperties.getProperty("jiraUrl");
			this.javaV1Path = studyProperties.getProperty("v1path");
			this.javaV2Path = studyProperties.getProperty("v2path");
			this.tablePath = studyProperties.getProperty("tablePath");
			this.blackListPath = studyProperties.getProperty("blackListPath");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstace() {
        if(config==null){
            config = new Config();
            return config;
        }
        else{
            return config;
        }
    }

    public String getProjectPath(){
        return projectPath;
    }

    public String getJiraIssuePattern() {
        return this.jiraIssuePattern;
    }

    public String getJiraUrl() {
        return this.jiraUrl;
    }
	public String getJavaV1Path() {
		return javaV1Path;
	}
	public void setJavaV1Path(String javaV1Path) {
		this.javaV1Path = javaV1Path;
	}
	public String getJavaV2Path() {
		return javaV2Path;
	}
	public void setJavaV2Path(String javaV2Path) {
		this.javaV2Path = javaV2Path;
	}
	public String getTablePath() {
		return tablePath;
	}
	public String getBlackListPath() {
		return blackListPath;
	}
	public void setBlackListPath(String blackListPath) {
		this.blackListPath = blackListPath;
	}
	public String getJiraId() {
		return jiraId;
	}
	public void setJiraId(String jiraId) {
		this.jiraId = jiraId;
	}	
}