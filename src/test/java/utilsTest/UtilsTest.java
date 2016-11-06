package utilsTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import core.Config;
import junit.framework.Assert;
import miner_pojos.FileInfo;
import table_builder.TableInfoCreator;
import utils.CalculateTableInfo;
import utils.GitReader;
import utils.PathFilters;



public class UtilsTest {


	/**
	 * Test that filters are working properly for all *.java files
	 */
	@Test
	public void filterEndingOnlyJavaExtensionPathFilterTest(){
		String path ="/foo/test.java";
		String extension = "java";
		boolean expectedValue = PathFilters.checkPathFilterEnding(path, extension);
		assertTrue(expectedValue);
	}
	
	@Test
	public void filterEndingOnlyNotJavaExtensionPathFilterTest(){
		String path ="/foo/test.txt";
		String extension = "java";
		boolean expectedValue = PathFilters.checkPathFilterEnding(path, extension);
		assertFalse(expectedValue);
	}
	
	/**
	 * Reads the git information of this project to test log information
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void readGitPathsTest() throws IOException, InterruptedException{
		List<Path> listOfPathFiles = GitReader.readGitJavaPaths(Paths.get("E:\\MiningRepositories\\workspace\\assigment1"));
		assertTrue("File Size is Zero",listOfPathFiles.size()>0);
		for(Path path : listOfPathFiles){
			boolean expectedValue = PathFilters.checkPathFilterEnding(path.toAbsolutePath().toString(), "java");
			assertTrue(expectedValue);
		}
	}
	
	/**
	 * Reads commits from a fake git repository
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void readFileCommitsTest() throws IOException, InterruptedException {
		String since = "2014-01-01";
		String until = "2016-12-25";
		List<Path> listOfPathFiles = GitReader.readGitJavaPaths(Paths.get("E:\\MiningRepositories\\workspace\\assigment1"));
		String path = listOfPathFiles.get(0).toString().replaceAll("/", "\\");
		GitReader gitReader = new GitReader();
		Map<String, Integer> map = gitReader.readFileCommitsFromDevelopers("E:/MiningRepositories/workspace/","E:/MiningRepositories/workspace/assigment1/"+path, since, until);
		assertTrue("File Size is Zero",map.size()>0);
		
	}
	
	@Test
	public void calculateMajorInfoTest() throws IOException, InterruptedException{
		Map<String, Integer> devMap = new HashMap();
		devMap.put("DevOwner", 80);
		devMap.put("DevMajorA", 15);
		devMap.put("DevMinor1", 1);
		devMap.put("DevOwner2", 1);
		devMap.put("DevOwner3", 1);
		devMap.put("DevOwner4", 1);
		devMap.put("DevOwner5", 1);
		
		Integer developerMajor =CalculateTableInfo.calculateMajorContributors(devMap);
		assertTrue("Major should be 1",developerMajor==2);
		
	}
	
	@Test
	public void calculateMinorInfoTest() throws IOException, InterruptedException{
		Map<String, Integer> devMap = new HashMap();
		devMap.put("DevOwner", 80);
		devMap.put("DevMajorA", 15);
		devMap.put("DevMinor1", 1);
		devMap.put("DevOwner2", 1);
		devMap.put("DevOwner3", 1);
		devMap.put("DevOwner4", 1);
		devMap.put("DevOwner5", 1);
		
		Integer developerMinor =CalculateTableInfo.calculateMinorContributors(devMap);
		assertTrue("Minors shoud be 5",developerMinor==5);
		
	}
	
	@Test
	public void calculateOwnerInfoTest() throws IOException, InterruptedException{
		Map<String, Integer> devMap = new HashMap();
		devMap.put("DevOwner", 80);
		devMap.put("DevMajorA", 15);
		devMap.put("DevMinor1", 1);
		devMap.put("DevOwner2", 1);
		devMap.put("DevOwner3", 1);
		devMap.put("DevOwner4", 1);
		devMap.put("DevOwner5", 1);
		Double ownerPercentage =CalculateTableInfo.calculateOwnership(devMap);
		assertTrue("Oner Percentage is "+ownerPercentage, ownerPercentage==0.80);
	} 
	
	@Test
	public void createFileInfoTest() throws IOException, InterruptedException {
		String since = "2014-01-01";
		String until = "2016-12-25";
		String projectPath ="E:\\MiningRepositories\\workspace\\assigment1";
		String gitProjectPath ="E:\\MiningRepositories\\workspace\\";
		 
		List<FileInfo> tableInfo = TableInfoCreator.createTable(gitProjectPath,projectPath, since, until);
		
		for(FileInfo fi:tableInfo){
			System.out.println(fi.toString());
		}
		assertTrue("File Info Test",tableInfo.size()>0);
		
	}

	@Test
	public void commitPojoTest(){

	}
	
	
	@Test
	public void matchTest() {
		String commitMessage = "Introduce jdiff Gradle taskThe new jdiff task generates a report of API differences between "
				+ "thecurrent version (i.e. the value of `version` in gradle.properties) "
				+ "andany older version of the framework, as specified by "
				+ "-DOLD_VERSION atthe command line, or defaulting to `previousVersion` ingradle.properties.Running the command requires a separate clone directory pinned to thedesired old version, as specified by -DOLD_VERSION_ROOT at the commandline. This creates challenges from a build automation perspective,largely because Gradle doesn't (yet) have APIs for working with Git.This task may be further automated and included in nightly CI runs, butin the meantime, a number of reports back to 3.1.3.RELEASE have beengenerated manually and uploaded to [1], where one can now find thefollowing entries in the directory listing - 3.1.3.RELEASE_to_3.2.0.RC1 - 3.2.0.M1_to_3.2.0.M2 - 3.2.0.M2_to_3.2.0.RC1 - 3.2.0.RC1_to_3.2.0.BUILD-SNAPSHOTIdeally, the final entry there would be kept up-to-date on a dailybasis - again we may revisit doing so in the future. Going forward,reports will be generated and uploaded manually on an as needed basisand as part of the release process.The goal of these reports are as follows - to ease the process of ensuring backward compatibility - to aid in code reviews, particularly when reviewing large pull   requests - to ease the process of creating migration guides for project   maintainers, i.e. to help us remember what's changed - to allow ambitious end-users to discover what's been changing at the   API level without without needing to wait for detailed  and/or migration guide documentationSee documentation in jdiff.gradle for usage details.Note that the jdiff-1.1.1 distribution as downloaded from [2] has beenadded wholesale to the source tree under gradle/jdiff instead ofuploading JDiff jars to repo.springsource.org as we would normally do.This is due to some unfortunate limitations in the implementation of thejdiff ant task that require a phisical JDIFF_HOME directory. Checking inthe jars and various resources represents the simplest and mostpragmatic solution to this problem, though ambitious contributors arefree to do what's necessary to arrive at a more elegant arrangement.[1] http//static.springframework.org/spring-framework/docs[2] http//sourceforge.net/projects/javadiff/files/latest/downloadIssue SPR-9957";
		String jiraId = Config.getInstace().getJiraId();
		String xx="SPR-0111";
		//String jiraId="SPR-[0-9]+";
		Pattern p = Pattern.compile(jiraId);
		//Pattern p = Pattern.compile(jiraId);
		String x = "";
		Matcher m = p.matcher(commitMessage);
		if(m.find()){
		     System.out.println(m.group());
		     x = m.group();
		     System.out.println("x: "+x);
		}
		Assert.assertTrue(x.length()>0);
	}
	
}