package utils;

import miner_pojos.DiffInfo;
import org.apache.commons.lang3.builder.Diff;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mey on 9/26/2016.
 */
public class GitInfo {

    private static final Pattern lineChangesPattern = Pattern.compile("@@ .* @@");


    /**
     diff --git a/solr/CHANGES.txt b/solr/CHANGES.txt
     index 91c39af..7af5815 100644
     --- a/solr/CHANGES.txt
     +++ b/solr/CHANGES.txt
     @@ -423,6 +423,9 @@

      * SOLR-4238: Fix jetty example requestLog config (jm via hossman)

     +* SOLR-4251: Fix SynonymFilterFactory when an optional tokenizerFactory is supplied.
     +  (Chris Bleakley via rmuir)
     +
     Other Changes
     ----------------------
      * */
    public static List<DiffInfo> extractFromGitDiffModifiedLines(String diffResult){
        //Get this line from diff "@@ -423,6  +423,9 @@"
        String modificationLine = extractModificationInfo(diffResult);
        String[] removedAddedPairs = modificationLine.replaceAll("@@","").trim().split(" ");
        List<DiffInfo> diffInfoList = new ArrayList<>();
        for(String modLinePair:removedAddedPairs){
            String[] diff = modLinePair.split(",");
            Integer mod = Integer.parseInt(diff[0]);
            Integer line = Integer.parseInt(diff[1]);
            DiffInfo diffInfo = new DiffInfo(mod,line);
           diffInfoList.add(diffInfo);
        }
        return diffInfoList;
    }

    private static String extractModificationInfo(String diffMessage) {
            Matcher m = lineChangesPattern.matcher(diffMessage);
        if(m.find()){
            return m.group();
        }else{
            return "";
        }
    }
}
