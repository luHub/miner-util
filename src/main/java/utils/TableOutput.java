package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import miner_pojos.CommitInfo;
import miner_pojos.FileInfo;

public class TableOutput {

	private static String RESULTS="table";
	
	public static void createResults(String fileName, List<FileInfo> tableInfo) throws IOException {
		Path path = Paths.get(fileName);
		if (Files.exists(path)) {
			Files.delete(path);
		}
		Files.createFile(path);
		Charset charset = Charset.forName("US-ASCII");
		BufferedWriter writer = Files.newBufferedWriter(path, charset);
		for(FileInfo fileInfo: tableInfo) {
			  writer.write(fileInfo.toString()+"\n");
			}
		writer.flush();
		writer.close();
	}

	public static void createCSVFromCommitInfo(String fileName, Map<String, CommitInfo> commitInfoMap) throws IOException {
		Path path = Paths.get(fileName);
		if (Files.exists(path)) {
			Files.delete(path);
		}
		Files.createFile(path);
		Charset charset = Charset.forName("US-ASCII");
		BufferedWriter writer = Files.newBufferedWriter(path, charset);

		Iterator it = commitInfoMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			writer.write(pair.getValue().toString()+"\n");
			it.remove(); // avoids a ConcurrentModificationException
		}
		writer.flush();
		writer.close();
	}
}