package utils;

import java.util.Map;

public class CalculateTableInfo {
	
	private final static double UPER_LIMIT = 20.0;
	private final static double LOWER_LIMIT = 5.0;
	private final static double ONE_HUNDRED_PERCENTAGE = 100.0;

	public static int calculateTotalContributors(Map<String,Integer> devInformationMap){
		return devInformationMap.size();
	}
	
	public static double calculateTotalCommitsPerFile(Map<String,Integer> devInformationMap){
		double totalCommits=0;
		for (Map.Entry<String, Integer> entry : devInformationMap.entrySet())
		{
			 totalCommits += entry.getValue();
		}
		
		return totalCommits;
	}
	
	
	public static int calculateMinorContributors(Map<String,Integer> devInformationMap){
		int minorCounter=0;
		for (Map.Entry<String, Integer> entry : devInformationMap.entrySet())
		{
			double proportion = (double)entry.getValue() / calculateTotalCommitsPerFile(devInformationMap);//ONE_HUNDRED_PERCENTAGE;
			if(proportion < LOWER_LIMIT/ONE_HUNDRED_PERCENTAGE)
				minorCounter++;
		}
		
		return minorCounter;
	}
	
	//TODO Check this method using a stub test to force each case
	public static int calculateMajorContributors(Map<String,Integer> devInformationMap){
		int majorCounter=0;
		for (Map.Entry<String, Integer> entry : devInformationMap.entrySet())
		{
			double proportion = (double)(entry.getValue()) / calculateTotalCommitsPerFile(devInformationMap);//ONE_HUNDRED_PERCENTAGE;
			if(proportion >= LOWER_LIMIT/ONE_HUNDRED_PERCENTAGE)
				majorCounter++;
		}
		
		return majorCounter;
	}
	
	public static double calculateOwnership(Map<String,Integer> devInformationMap){
		double ownerMaxProportion=0;
		for (Map.Entry<String, Integer> entry : devInformationMap.entrySet())
		{
			double proportion = (double)(entry.getValue() / calculateTotalCommitsPerFile(devInformationMap));
			if(proportion >= UPER_LIMIT/ONE_HUNDRED_PERCENTAGE && proportion > ownerMaxProportion )
				ownerMaxProportion=proportion;
		}
		
		return ownerMaxProportion;
	}
	
}
