package core;

import utils.GitReader;
import utils.JiraReader;

/**
 * Created by mey on 10/19/2016.
 */
public class MainSpringStudy {

    public static void main(String[] args){
        System.out.print("QUICK AND DIRTY BECAUSE NO TIME LATER WE WILL BE DOING BETTER\n");

        //         <-|            <-|          <-|         <-|         <-|
        // --Dev V1--|-----V1-------|------V2----|-----V3----|-----V4----|
        //               |--Dev V2--| |--Dev V3--|
        //TODO Next time... always next time!
        //From maven and based on the X.Y.Z versioning system
        //Major Versions:
        String V1="1.0%20final";
        String V2="2.0%20final";
        String V3="3.0.1";
        String V4="4.0.1"; 

        System.out.print("Major"+"\n");
        //String v1Url="https://jira.spring.io/rest/api/2/search?jql=project%20%3D%20SPR%20AND%20issuetype%20%3D%20Bug%20AND%20(%20affectedVersion%20>%20\"1.0%20final\"%20AND%20affectedVersion%20<%20\"2.0%20final\")";
        String v1Url="https://jira.spring.io/rest/api/2/search?jql=project%20%3D%20SPR%20AND%20issuetype%20%3D%20Bug%20AND%20(%20affectedVersion%20<=%20\""+V1+"\")";
        int v1Bugs = JiraReader.getBugNumber(v1Url);
        System.out.print("V1: "+v1Bugs+"\n");

        String v2Url="https://jira.spring.io/rest/api/2/search?jql=project%20%3D%20SPR%20AND%20issuetype%20%3D%20Bug%20AND%20(%20affectedVersion%20>%20\""+V1+"\"%20AND%20affectedVersion%20<=%20\""+V2+"\")";
        int v2Bugs = JiraReader.getBugNumber(v2Url);
        System.out.print("From V1 to V2: "+v2Bugs+"\n");

        String v3Url="https://jira.spring.io/rest/api/2/search?jql=project%20%3D%20SPR%20AND%20issuetype%20%3D%20Bug%20AND%20(%20affectedVersion%20>%20\""+V2+"\"%20AND%20affectedVersion%20<%20\""+V3+"\")";
        int v3Bugs = JiraReader.getBugNumber(v3Url);
        System.out.print("From V2 to V3: "+v3Bugs+"\n");

        String v4Url="https://jira.spring.io/rest/api/2/search?jql=project%20%3D%20SPR%20AND%20issuetype%20%3D%20Bug%20AND%20(%20affectedVersion%20>%20\""+V3+"\"%20AND%20affectedVersion%20<%20\""+V4+"\")";
        int v4Bugs = JiraReader.getBugNumber(v4Url);
        System.out.print("From V3 to V4: "+v4Bugs+"\n");

        String totalUrl="https://jira.spring.io/rest/api/2/search?jql=project%20%3D%20SPR%20AND%20issuetype%20%3D%20Bug%20AND%20(%20affectedVersion%20<%20\""+V4+"\")";
        int totalBugs = JiraReader.getBugNumber(totalUrl);
        System.out.print("total: "+totalBugs+"\n");

        System.out.print("Error Overlapping: "+(totalBugs-v1Bugs-v2Bugs-v3Bugs-v4Bugs)+"\n");


        System.out.print("Minors\n");
       // String[] V1_Minors={"1.0%20M1","1.0%20M2","1.0%20M3","1.0%20M4","1.0%20RC1","1.0%20RC2"};
       // String v1minorsM4="https://jira.spring.io/rest/api/2/search?jql=project%20%3D%20SPR%20AND%20issuetype%20%3D%20Bug%20AND%20(affectedVersion%20<=%20\""+V1_Minors[0]+"\")";
       // int minorNumberRC1 = JiraReader.getBugNumber(v1minorsM4);
       // System.out.print(V1_Minors[0].replace("%20"," ")+ ": "+minorNumberRC1+"\n");
       // minorsBugCount(V1_Minors);

        //V1 [1.1.1 - 1.2.1][1.2.1 - ]
        System.out.print("Minors V1\n");
        String[] V1_Minors={"1.0%20final","1.1.1","1.2.1","1.2.8"};
        minorsBugCount(V1_Minors);

        //V2 Missing one part [2.0 final - 2.5.1][2.51-End]
        System.out.print("Minors V2\n");
        String[] V2_Minors={"2.0%20final","2.5.1","2.5.6"};
        minorsBugCount(V2_Minors);

        //V3
        System.out.print("Minors V3\n");
        String[] V3_Minors={"3.0.1","3.1.1","3.2.1","3.2.17"};
        minorsBugCount(V3_Minors);

        //V4
        System.out.print("Minors V4\n");
        String[] V4_Minors={"4.0.1","4.1.1","4.2.1","4.3.1",};
        minorsBugCount(V4_Minors);

        //Only in Releases
        System.out.print("Only in Releases\n");
        bugCount(V1);
        bugCount(V2);
        bugCount(V3);
        bugCount(V4);
        System.out.print("Only Minor Releases\n");
        for(String v: V1_Minors){
            bugCount(v);
        }
        for(String v: V2_Minors){
            bugCount(v);
        }
        for(String v: V3_Minors){
            bugCount(v);
        }
        for(String v: V4_Minors){
            bugCount(v);
        }




    }

    private static void minorsBugCount(String[] versionArray) {
        for(int i = 0; i+1< versionArray.length; i++){
            String v1minors="https://jira.spring.io/rest/api/2/search?jql=project%20%3D%20SPR%20AND%20issuetype%20%3D%20Bug%20AND%20(%20affectedVersion%20>%20\""+ versionArray[i]+"\"%20AND%20affectedVersion%20<=%20\""+ versionArray[i+1]+"\")";
            int minorNumber = JiraReader.getBugNumber(v1minors);
            System.out.print(versionArray[i+1].replace("%20"," ")+ ": "+minorNumber+"\n");
        }
    }

    private static void bugCount(String version) {
            String v1minors="https://jira.spring.io/rest/api/2/search?jql=project%20%3D%20SPR%20AND%20issuetype%20%3D%20Bug%20AND%20affectedVersion%20=%20\""+ version+"\"";
            int minorNumber = JiraReader.getBugNumber(v1minors);
            System.out.print(version.replace("%20"," ")+ ": "+minorNumber+"\n");
    }
}
