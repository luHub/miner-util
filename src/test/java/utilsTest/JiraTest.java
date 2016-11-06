package utilsTest;

import core.Config;
import org.junit.Test;
import utils.JiraReader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by LuHUb on 9/25/2016.
 */

/**
 * This Test are only ment to work with LUCENE Project because of its jira CommitInfo Styling
 */
public class JiraTest {
    @Test
    public void jiraLuceneIsBugTest1(){
        String jiraUrl ="https://issues.apache.org/jira/rest/api/2/issue/";
        String luceneJavaBug="LUCENE-7228";
        boolean isBug=JiraReader.IsBug(luceneJavaBug, jiraUrl);
        assertTrue(isBug);
    }
    @Test
    public void jiraLuceneIsNotBugTest1(){
        String jiraUrl ="https://issues.apache.org/jira/rest/api/2/issue/";
        String luceneFeature="LUCENE-7381";
        boolean isBug=JiraReader.IsBug(luceneFeature, jiraUrl);
        assertFalse(isBug);
    }
}

