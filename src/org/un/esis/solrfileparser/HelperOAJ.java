package org.un.esis.solrfileparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Author: Kevin Bradley
//Date: 11-June-2014
//Description: This is a general helper class for OAJ used to assist with business logic of OAJ
//Version: 1.0
//Code Reviewer:
public class HelperOAJ {

	// This helper method for OAJ is used to clean judges names
	// Returns: String
    public static String cleanJudge(String init) {
    	String cleansed = init.substring(init.lastIndexOf(" ")+1).trim();
    	
    	return cleansed.toLowerCase().replace("greffe", "")
    			.replace("president", "")
    			.replace("président", "")
    			.replace("presiding", "")
    			.replace("duty", "")
    			.replace("(presiding)", "")
    			.replace("(","")
    			.replace(")","");	
    }
    
    // This helper method for OAJ is used to convert French regsitry names to English and make all uppercase
    // Returns: String
    public static String convertRegistry(String init) {   	
    	String cleaned = init.toLowerCase().replace("geneve", "geneva").replace(".", "");
    	if (cleaned.contains("new york") ||cleaned.contains("geneva") || cleaned.contains("nairobi"))
    		return cleaned.toUpperCase();
    	return "NOT EXTRACTED";
    }
    
    // This helper method for OAJ is used to translate French months to English
    // Returns: String
    public static String getEnglishVersionOfDate(String init) {
    	Pattern p = Pattern.compile("^\\d{1,2}\\s+\\w*\\s+\\d{4}");
    	Matcher m = p.matcher(init);
    	if (m.matches()) {
    		init = init.trim().replaceAll(" +", " ");
	    	if (init.contains("janvier") || init.contains("january")) {
	    		init = init.replace("janvier", "january");
	    		return init.contains("january") ? init : "01 January 1900";
	    	} else if (init.contains("février") || init.contains("february")) {
	    		init =  init.replace("février", "february");
	    		return init.contains("february") ? init : "01 January 1900";
	    	} else if (init.contains("mars") || init.contains("march")) {
	    		init = init.replace("mars", "march");
	    		return init.contains("march") ? init : "01 January 1900";
	    	} else if (init.contains("avril") || init.contains("april")) {
	    		init =  init.replace("avril", "april");
	    		return init.contains("april") ? init : "01 January 1900";
	    	}else if (init.contains("mai") || init.contains("may")) {
	    		init =  init.replace("mai", "may");
	    		return init.contains("may") ? init : "01 January 1900";
	    	}else if (init.contains("juin") || init.contains("june")) {
	    		init =  init.replace("juin", "june");
	    		return init.contains("june") ? init : "01 January 1900";
	    	}else if (init.contains("juillet") || init.contains("july")) {
	    		init = init.replace("juillet", "july");
	    		return init.contains("july") ? init : "01 January 1900";
	    	}else if (init.contains("août") || init.contains("august")) {
	    		init = init.replace("août", "august");
	    		return init.contains("august") ? init : "01 January 1900";
	    	}else if (init.contains("septembre") || init.contains("september")) {
	    		init = init.replace("septembre", "september");
	    		return init.contains("september") ? init : "01 January 1900";
	    	}else if (init.contains("octobre") || init.contains("october")) {
	    		init = init.replace("octobre", "october");
	    		return init.contains("october") ? init : "01 January 1900";
	    	}else if (init.contains("novembre") || init.contains("november")) {
	    		init = init.replace("novembre", "november");
	    		return init.contains("november") ? init : "01 January 1900";
	    	}else if (init.contains("décembre") || init.contains("decembre") || init.contains("december")) {
	    		init = init.replace("décembre", "december").replace("decembre", "december");
	    		return init.contains("december") ? init : "01 January 1900";
	    	} else
	    		return "01 January 1900";
    	}
    	else
    	{
    		return "01 January 1900";
    	}
    }
}
