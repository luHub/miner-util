package utilsTest;

import miner_pojos.CommitInfov2;
import miner_pojos.DiffInfo;
import org.junit.Test;
import utils.GitInfo;
import utils.GitReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by mey on 9/26/2016.
 */
public class GitInfoTest {

    @Test
    public void gitFileModificationsLinesTest(){
        String diffInformation ="diff --git a/solr/CHANGES.txt b/solr/CHANGES.txt\n" +
                "index 91c39af..7af5815 100644\n" +
                "--- a/solr/CHANGES.txt\n" +
                "+++ b/solr/CHANGES.txt\n" +
                "@@ -423,6 +423,9 @@\n" +
                " \n" +
                " * SOLR-4238: Fix jetty example requestLog config (jm via hossman)\n" +
                " \n" +
                "+* SOLR-4251: Fix SynonymFilterFactory when an optional tokenizerFactory is supplied.\n" +
                "+  (Chris Bleakley via rmuir)\n" +
                "+\n" +
                " Other Changes\n" +
                " ----------------------\n" +
                " \n";

        List<DiffInfo> diffInfoList =GitInfo.extractFromGitDiffModifiedLines(diffInformation);
        int modLine = diffInfoList.get(0).getLinesModifications();
        assertTrue(diffInfoList.get(0).getLinesModifications()==-423);
        assertTrue(diffInfoList.get(0).getLineNumber()==6);
        assertTrue(diffInfoList.get(1).getLinesModifications()==423);
        assertTrue(diffInfoList.get(1).getLineNumber()==9);
    }

    @Test
    public void getCommitsFromFileTest() throws IOException, InterruptedException {
        Path path = Paths.get("E:\\MiningRepositories\\lune\\lucene-solr\\solr\\core\\src\\java\\org\\apache\\solr\\analysis\\package-info.java");
        Path projectPath = Paths.get("E:\\MiningRepositories\\lune\\lucene-solr");
        List<CommitInfov2> hashList = GitReader.getCommitsFromFile(projectPath,path);
        assertTrue(hashList.size()>0);
    }

    public void getShowTest() throws IOException, InterruptedException {
        Path path = Paths.get("E:\\MiningRepositories\\lune\\lucene-solr\\solr\\core\\src\\java\\org\\apache\\solr\\analysis\\package-info.java");
        Path projectPath = Paths.get("E:\\MiningRepositories\\lune\\lucene-solr");
        List<CommitInfov2> hashList = GitReader.getCommitsFromFile(projectPath,path);
        //String file=GitReader.gitGetFileVersion(projectPath,path,hashList.get(0).getHash());
       // assertTrue(file.length()>0);
    }
    //THink to use checkout and later load the version we want to compare using
}
