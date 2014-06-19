package org.un.esis.solrfileparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Author: Kevin Bradley
//Date: 11-June-2014
//Description: This is a general helper class for ODS used to assist with business logic of ODS
//Version: 1.0
//Code Reviewer:
public class HelperODS {

	// This helper method for OAJ is used to clean judges names
	// Returns: String
    static String getContentFilenameCleaned(String filename)
    {
    	try {
			String contentFilenameStartIden = AppProp.contentFilenameStartIdentifer;
			String contentFilename = filename.substring(filename.indexOf(contentFilenameStartIden)+contentFilenameStartIden.length(), filename.length());
			return contentFilename.replace("/", AppProp.systemSeperator);
    	} catch (Exception e) {
    		Helper.recordError("ERROR: Extracting content filename - " + e.getMessage());
    		return null;
    	}
    }
    
    static List<String> getReferences(String fulltext)
    {
    	List<String> refs = new ArrayList<String>();
		Pattern pattern = Pattern.compile("(\\w+[\\-\\./\\(\\)]\\w+[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*[\\-\\./\\(\\)]*\\w*)");
    	//Pattern pattern = Pattern.compile("[A-Z]+\\/{1}\\w+[\\.\\-\\/]*\\w+[\\.\\-\\/]*\\w*[\\.\\-\\/]*\\w*"); // ACC/1990/DEC/1-14
    	Matcher matcher = pattern.matcher(fulltext);
		while (matcher.find()) {
			String potentialMatch = matcher.group();
			if (potentialMatch.indexOf("/") > 0) {
				if (!refs.contains(potentialMatch))
					refs.add(potentialMatch);
			}
		}
		return refs;
    }
    
    static List<String> getSpecifcReference(String startIndicator, List<String> fullReferences, List<String> subtypes) {
    	List<String> specificRefs = new ArrayList<String>();
    	for(String ref : fullReferences){
    		String currRef = ref.toUpperCase();
    	    if (currRef.startsWith(startIndicator.toUpperCase())) {
    	    	if (!specificRefs.contains(currRef)) {
    	    		if (subtypes.isEmpty())
    	    			specificRefs.add(cleanReferenceLastCharacter(currRef));
    	    		else {
    	    			boolean skip = false;
    	    			for(String st : subtypes) {
    	    				if (currRef.startsWith(st))
    	    					skip = true;
    	    			}
	    				if (!skip)
	    					specificRefs.add(cleanReferenceLastCharacter(currRef));
    	    		}
    	    	}
    	    }
    	}
    	return specificRefs;
    }
    
    static String cleanReferenceLastCharacter(String ref) {
    	if (ref != null && !ref.equals("")) {
    		String[] removeChars = { ")", ",", "." };
    		boolean containedRC = false;
    		for(String c : removeChars) {
    			String lastChar = ref.substring(ref.length()-1);
    			if (lastChar.equals(c)) {
    				ref = ref.substring(0, ref.length()-1);
    				containedRC = true;
    			}
    		}
    		if (containedRC) {
    			return cleanReferenceLastCharacter(ref);
    		}else
    			return ref;
    	}
    	else
    		return "";
    }
    
    static String getDocumentType(String sym) {
    	String cleanedSymbol = sym.toUpperCase();
    	if (cleanedSymbol.startsWith("A/")) {
    		
	    	if (cleanedSymbol.startsWith("A/C.1"))
	    		return "First Committee";
	    	else if (cleanedSymbol.startsWith("A/C.2"))
	    		return "Second Committee";
	    	else if (cleanedSymbol.startsWith("A/C.3"))
	    		return "Third Committee";
	    	else if (cleanedSymbol.startsWith("A/C.4"))
	    		return "Fourth Committee";
	    	else if (cleanedSymbol.startsWith("A/C.5"))
	    		return "Fifth Committee";
	    	else if (cleanedSymbol.startsWith("A/C.6"))
	    		return "Sixth Committee";
	    	else
	    		return "General Assembly";
    	}

    	
    	if (cleanedSymbol.startsWith("ST/")) {
    		
	    	if (cleanedSymbol.startsWith("ST/IC"))
	    		return "Information Circulars (ST/IC)";
	    	else if (cleanedSymbol.startsWith("ST/AI"))
	    		return "Admin. Instructions (ST/AI)";
	    	else if (cleanedSymbol.startsWith("ST/SGB"))
	    		return "SG's Bulletins";
	    	else
	    		return "Secretariat";
    	}
    	
    	if (cleanedSymbol.startsWith("S/"))
    		return "Security Council";
    	
    	if (cleanedSymbol.startsWith("E/") || cleanedSymbol.startsWith("LC/") || cleanedSymbol.startsWith("ECE/")) {
    		
    		if (cleanedSymbol.startsWith("E/ECA"))
	    		return "ECA";
	    	else if (cleanedSymbol.startsWith("E/ESCAP"))
	    		return "ESCAP";
	    	else if (cleanedSymbol.startsWith("E/ESCWA"))
	    		return "ESCWA";
	    	else if (cleanedSymbol.startsWith("ECE/"))
	    		return "ECE";
	    	else if (cleanedSymbol.startsWith("LC/"))
	    		return "ECLAC";
	    	else
	    		return "Economic and Social Council";
    	}
    	
    	if (cleanedSymbol.startsWith("T/"))
    		return "Trusteeship Council";
    	
    	if (cleanedSymbol.startsWith("A/HRC"))
    		return "Human Rights Council";

    	if (cleanedSymbol.startsWith("AT/"))
    		return "Administrative Tribunal";
    	
    	if (cleanedSymbol.startsWith("APLC/"))
    		return "Anti-Personnel Landmines Convention";
    	
    	if (cleanedSymbol.startsWith("CERD/"))
    		return "International Convention on the Elimination of All Forms of Racial Discrimination";
    	
    	if (cleanedSymbol.startsWith("DP/"))
    		return "United Nations Development Programme";
    	
    	if (cleanedSymbol.startsWith("ID/"))
    		return "United Nations Industrial Development Organization";
    	
    	if (cleanedSymbol.startsWith("TD/"))
    		return "United Nations Conference on Trade and Development";
    	
    	if (cleanedSymbol.startsWith("UNEP/"))
    		return "United Nations Environment Programme";
    	
    	if (cleanedSymbol.startsWith("UNITAR/"))
    		return "United Nations Institute for Training and Research";
    	
    	if (cleanedSymbol.startsWith("WFC/"))
    		return "World Food Council";

    	return "N/A";

    }
}
