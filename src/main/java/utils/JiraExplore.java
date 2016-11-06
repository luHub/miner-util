package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mey on 9/26/2016.
 */
public class JiraExplore {

    @Deprecated
    public static String readIssueId(String commitMessage, Pattern idPattern){
        if(isIssue(commitMessage,idPattern)){
        return commitMessage;
        }
        return "";
        }


    public static boolean isIssue(String commitMessage, Pattern idPattern) {
        Matcher m = idPattern.matcher(commitMessage);
        return m.find();
    }
}
