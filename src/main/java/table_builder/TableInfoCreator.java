package table_builder;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import miner_pojos.FileInfo;
import utils.CalculateTableInfo;
import utils.GitReader;

public class TableInfoCreator {

	
	/**
	 * 
	 * @param projectPath Root Path of the project generally here is the .git 
	 * @param since Date as YYYY-MM-DD Format "2014-01-01"
	 * @param until Date as YYYY-MM-DD Format "2014-01-01"
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static List<FileInfo> createTable(String gitProjectPath,String projectPath, String since, String until)
			throws IOException, InterruptedException {
		List<Path> listOfPathFiles = GitReader.readGitJavaPaths(Paths.get(projectPath));
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		for (Path filePath : listOfPathFiles) {
			GitReader gitReader = new GitReader();
			System.out.println("Reading Commits in File: "+filePath);
			Map<String, Integer> devMap = gitReader.readFileCommitsFromDevelopers(gitProjectPath,projectPath+"\\"+ filePath.toAbsolutePath().toString().replace("/", "\\"), since, until);
			fileInfoList.add(FileInfoCreator.createFileInfo(filePath.toAbsolutePath().toString(), devMap));
		}
		return fileInfoList;
	}

	//TODO test this method
	public static FileInfo createRow(String gitProjectPath,String fileName,String projectPath, String since, String until)
			throws IOException, InterruptedException {
			GitReader gitReader = new GitReader();
			Map<String, Integer> devMap = gitReader.readFileCommitsFromDevelopers(gitProjectPath.replace("/","\\"),(gitProjectPath+"/"+projectPath).replace("/","\\"), since, until);
			return FileInfoCreator.createFileInfo(fileName, devMap);
	}


}
