package utils;

import java.util.regex.Pattern;

public class PathFilters {
	
	/**
	 * returns true if the path contains the mathed extension,
	 * the extension must not include the period (.).
	 * Example:
	 * (.../foo/example.java , java)
	 * @param regex
	 * @return 
	 */
	public static boolean checkPathFilterEnding(String path,String extension){
		return Pattern.matches(".*\\."+extension, path);
	}


}