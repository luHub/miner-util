package utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JiraReader {

    public static boolean IsBug(String issueId,String jiraUrl){
        try {
            //Example "https://issues.apache.org/jira/rest/api/2/issue/"
            String url = jiraUrl + issueId;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //Replace this part
            String bugLine = readBugLine(response.toString());
            if (bugLine.equals("Bug")){
                return true;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

        return false;
    }

    /**
     * Returns the Issue Response: it could be or not be BUG according to Lucene and Jira Docs
     * @param json
     * @return
     * @throws IOException
     */

    private static String readBugLine(String json) throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = mapper.readTree(json);
        //issue.fields.issuetype.name
        return rootNode.get("fields").get("issuetype").get("name").asText();
    }

    /** Jira All Bug Reader*/
    public static int getBugNumber(String url){
        StringBuffer response = new StringBuffer();
        int numberOfbugs=-1;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            numberOfbugs = getTotalItemsFromJQLQuery(response.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numberOfbugs;
    }

    //Read Total (Number of items from result)
    private static int getTotalItemsFromJQLQuery(String json) throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = mapper.readTree(json);
        //issue.fields.issuetype.name
        return Integer.parseInt(rootNode.get("total").asText());
    }
}
