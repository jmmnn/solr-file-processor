package org.un.esis.solrfileparser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.tika.language.LanguageIdentifier;

// Author: Kevin Bradley
// Date: 11-June-2014
// Description: This is a general helper class used to assist in common activites
// Version: 1.0
// Code Reviewer:
public class Helper {
	
	// This method is used to identify the hostname of the machine running the app
	// Returns: void
	static void setRunningMachine(){
		try {
			AppProp.runningMachine = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			recordError("ERROR: Obtaining machine name: " + e.getMessage());
		}
	}
	
	// This method is a wrapper for the properties object and obtains a property based on its id/name
	// Returns: String
	static String getProperty(String name){
		return AppProp.configFile.getProperty(name);
	}
	
	// This method is used to display the operating system where the application is running
	// Returns: void
	static void displayOS(){
		try {
			if (AppProp.OS.indexOf("win") >= 0) 
				System.out.println("Running on windows");
			else 
				System.out.println("Running on non windows");
		} catch (Exception e) {
			recordError("ERROR: Obtaining OS type: " + e.getMessage());
		}
	}
	
	// This method is used to initalise the Configuration file - it calls getProperties and sets the AppProp
	// Returns: void
	static void initialiseConfigFile(){
		try {
			AppProp.configFile = Helper.getProperties(Service.class.getClassLoader());
		} catch (Exception e) {
			System.out.println("ERROR: Error occured whilst attempting to open config file: " + e.getMessage());
		}
	}

	// This method is used to obtain the properties file and consequent properties for the application
	// Returns: Properties
    static Properties getProperties(ClassLoader loader) throws Exception {
    	Properties configFile = new Properties();
    	try {
    		if (AppProp.appType.equals("OAJ"))
    			configFile.load(loader.getResourceAsStream("oaj_config.properties"));
    		if (AppProp.appType.equals("ODS"))
    			configFile.load(loader.getResourceAsStream("ods_config.properties"));
		} catch (Exception e) {
			recordError("Error obtaining properties for type (" + AppProp.appType + ") - " + e.getMessage());
		}
    	return configFile;
    }
    
    // This method is used to obtain a Timestamp object for a particular date
    // Returns: Timestamp
	static Timestamp getTimestamp(String sdate){
		if (sdate == null || sdate.equals(""))
			sdate = "1900-01-01T00:00:00Z";
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(sdate);
		} catch (Exception e) {
			recordError("Error Parsing Date Time(" + sdate + ") - " + e.getMessage());
		}
		long time = date.getTime();
		return new Timestamp(time);
	}
	
	// This method is used to display and record an application error within the program
	// Returns: void
    static void recordError(String errorMessage) {
    	String fullMessage = "ERROR: " + AppProp.currentDate + " -  File [" + AppProp.currentlyProcessingFilename + "] OutputFile [" + AppProp.outputFilenameAdditions + "]: " + errorMessage;
    	System.out.println(fullMessage);
    	AppProp.log.error(fullMessage);
    	AppProp.outputDatabase.logError(fullMessage);
    }
    
    // This method is used to identify a single instance of a regular expression pattern
    // Returns: String
    static String getSingleValue(String fullText, String pattern, String[] keywords) {
    	Pattern p = Pattern.compile(pattern);
    	Matcher m = p.matcher(fullText.toLowerCase());
    	String vals = "";
    	while (m.find()){
    		vals = m.group();
    	}
    	for(String s : keywords) {
    		vals = vals.replace(s, "");
    	}
    	return vals.trim();
    }

    // This method is used to identify multiple instances of a regular expression pattern and store these in a String array
    // Returns: String array
    static String[] getMultiValue(String fullText, String startTextPattern, String endTextPattern, String splitter, String[] keywords) {
    	String lowerFullText = fullText.toLowerCase();
    	int startIndex = getIndexOf(lowerFullText, startTextPattern, -1); 
    	int endIndex = getIndexOf(lowerFullText, endTextPattern, startIndex);
    	int startTextLength = startTextPattern.length();
    	String vals;
    	if (startIndex < 0 || endIndex < 0)
    		return new String[] { "" };
    	else
    		vals = lowerFullText.substring(startIndex+startTextLength, endIndex);
    	for(String s : keywords) {
    		vals = vals.replace(s, "");
    	}
    	String cleaned = vals.trim();
    	return cleaned.split(splitter);
    }
    
    // This method is used to Format a date into a UNIX based format yyyy-MM-dd'T'HH:mm:ss'Z'
    // Returns: String
    static String getFormattedDate(String unformattedDate, String format) {
    	if (unformattedDate == null || unformattedDate.equals("")) {
    		unformattedDate = "01 January 1900";
    	}

	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    DateFormat sf = new SimpleDateFormat(format);
	    Date udate = null;
		try {
			udate = sf.parse(unformattedDate);
		} catch (Exception e) {
			recordError("Formatting Date Time(" + unformattedDate + ") - " + e.getMessage());
		}
	    return df.format(udate);
	}
    
    // This method is used to Camel Case text
    // Returns: String
    static String toCamelCase(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }
        return ret.toString();
    }
    
    // This method is used to obtain the index of a particular regular expression pattern
    // Returns: Int
    static int getIndexOf(String text, String regex, int startIndex) {
    	int index = -1;
    	Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        // Check all occurrences
        if (startIndex < 1) {
	        while (matcher.find()) {
	        	index = matcher.start();
	        	break;
	        }
        }
        else {
	        while (matcher.find(startIndex)) {
	        	index = matcher.start();
	        	break;
	        } 
        }
        return index;
    }
    
    // This method is used to display debug information
    // Returns: void
    static void displayInfo(String text) {
    	if (AppProp.debug) {
    		System.out.println(text + " " + AppProp.currentDate);
    		AppProp.log.info(text + " " + AppProp.currentDate);
    	}
    }
    
    // This method is used to identify the language code of text based on Tikas algorithm
    // Returns: String
    static String getLanguageCode(String fullText) {
    	LanguageIdentifier li = new LanguageIdentifier(fullText);
    	return li.getLanguage();
    }
    
    // This method is a wrapper for extractPDFData and is used when a non FTP connection is required
    // This method accesses file directly and passes this it to Tika
    // Returns: KV Pair of PDF data
    static Map<String, Object> extractDataViaShare(String filename) { 
    	Map<String, Object> data = null;
		try {		
			AppProp.currentlyProcessingFilename = filename;
		    File file = new File(filename);
		    data = extractFileData(file);
		} catch (Exception e) {
			Helper.recordError("Error extracting text via Share - " + e.getMessage());
		}
		return data;
    }
    
    // This method is a wrapper for extractPDFData and is used when an FTP connection is required
    // This method downloads a copy of the remote file locally and passes this local file to Tika
    // Returns: KV Pair of PDF data
    static Map<String, Object> extractDataViaFTP(String filename) { 
    	Map<String, Object> data = null;
		try {
			String localFilename = AppProp.outputFileDir + AppProp.tempTikaFTPFilename;

			AppProp.ftp.setFileType(FTP.BINARY_FILE_TYPE);
			AppProp.ftp.enterLocalPassiveMode();
			AppProp.ftp.setAutodetectUTF8(true);

		    //Create an InputStream to the File Data and use FileOutputStream to write it
		    InputStream inputStream = AppProp.ftp.retrieveFileStream(filename);
		    FileOutputStream fileOutputStream = new FileOutputStream(localFilename);
		    IOUtils.copy(inputStream, fileOutputStream);
		    fileOutputStream.flush();
		    boolean commandOK = AppProp.ftp.completePendingCommand();
		    
		    File file = null;
			if (commandOK) {
				file = new File(localFilename);
				data = extractFileData(file);
			}
		} catch (Exception e) {
			Helper.recordError("Error extracting text via FTP - " + e.getMessage());
		}
		return data;
    }
    
    // This method is used to connect to Tika and extract all the data available from the PDF
    // Returns: KV Pair of FullText and MetaData
    private static Map<String, Object> extractFileData(File file) { 
		try {
			ExtractorTika parser = new ExtractorTika();
			Map<String, Object> allPDFData = new HashMap<String, Object>();
			
			allPDFData = parser.extraction(file);
				
			Helper.displayInfo("INFO: Parsed data successfully for file" + file.getAbsolutePath());
					
			return allPDFData;	
			
		} catch (Exception e) {
			Helper.recordError("Error extracting text via FTP - " + e.getMessage());
		}
		return null;
    }
    
    // This method is used to call an external java jar file  
    // Returns: String
    static String callExternalJar(String jar, String properties, String params) {
		 try {
		    Process ps = Runtime.getRuntime().exec("java " + properties + " -jar " + jar + " " + params);
		    ps.waitFor();
		    InputStream is = ps.getInputStream();
		    byte b[] = new byte[is.available()];
		    is.read(b,0,b.length);
		    return new String(b);
		} catch (Exception e) {
			Helper.recordError("Error calling external jar [" + jar + "] - " + e.getMessage());
			return null;
		}
	};

	// This method is used to post the output file data to Solr
	// Returns: void
    static void postDataToSolr(String outputFilename) {
		 try {
		        System.out.println("Posting Output File to Solr: " + AppProp.solrCollection);
		        // Call the post.jar in the solr directory to push the output xml file to SOLR
		        String output = callExternalJar(AppProp.solrJarFile, "-Dcommit=true -Durl=" + AppProp.solrCollection, outputFilename);
		        System.out.println("Output from post.jar: " + output);
		        System.out.println("Called solr post.jar file");
	        } catch (Exception e) {
				Helper.recordError("Error calling post.jar - " + e.getMessage());
			}
	}
	
    // This method is used to close a currently open FTP connection
    // Returns: Boolean
	static boolean closeFTPConnection() {
        try {
        	if (AppProp.ftp.isConnected()) {
	    		Helper.displayInfo("INFO: Logging out of FTP Server");
				AppProp.ftp.logout();
		        Helper.displayInfo("INFO: Disconnecting from FTP Server");
		        AppProp.ftp.disconnect();
        	}
		} catch (IOException e) {
			Helper.recordError("Error closing FTP connection - " + e.getMessage());
			return false;
		}
        return true;
	}
	
	// This method is used to obtain an FTP connection to a server
	// Returns: Boolean
	static boolean getFTPConnection() {
		try {
        	// Connect to the FTP Server
        	Helper.displayInfo("INFO: Connecting to FTP Server [" + AppProp.ftpServerAddress + "]");
        	AppProp.ftp.connect(AppProp.ftpServerAddress);
			Helper.displayInfo("INFO: Connected to FTP Server [" + AppProp.ftpServerAddress + "]");
			Helper.displayInfo("INFO: Logging into to FTP Server with [Username:"  + AppProp.ftpUserId + "] and [Password:" + AppProp.ftpPassword + "]");

			if(AppProp.ftp.login(AppProp.ftpUserId, AppProp.ftpPassword))
	        {
				// Obtain the reply code and ensure its connected successfully
	        	int reply = AppProp.ftp.getReplyCode();
	            if (FTPReply.isPositiveCompletion(reply))
	            {
	            	Helper.displayInfo("INFO: Entering FTP passive mode");
	            	AppProp.ftp.enterLocalPassiveMode();
	            	Helper.displayInfo("INFO: Remote system is" + AppProp.ftp.getSystemType());
	            	return true;
	            }
	            else {
	            	return false;
	            }
	        } else
	        	return false;
		} catch (Exception e) {
			Helper.recordError("Initialising FTP connection (Server:" + AppProp.ftpServerAddress + ") - " + e.getMessage());
			return false;
		}
	}
	
	// This method is used to obtain the current Timestamp
	// Returns: Timestamp
	static Timestamp getCurrentTimestamp() {
		long time = System.currentTimeMillis();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
		return timestamp;
	}
	
	// This method id used to construct the full filename of the Solr Output File
	// Returns: void
	static void setCurrentOutputFilename(int currentOutputFileCount, boolean addition) {
		if (addition) {
			if (currentOutputFileCount == 0)
				AppProp.outputFilenameAdditions = AppProp.outputFileDir + AppProp.appType + "-OUTPUT-ADD_" + AppProp.currentDate + ".xml";
			else
				AppProp.outputFilenameAdditions = AppProp.outputFileDir + AppProp.appType + "-OUTPUT-" + currentOutputFileCount + "-ADD_" + AppProp.currentDate + ".xml";
		} else
			AppProp.outputFilenameDeletions = AppProp.outputFileDir + AppProp.appType + "-OUTPUT-DEL_" + AppProp.currentDate + ".xml";
	}
	
	// This method is used to iterate across files in a FTP file system and call a callback function - businessLogicFunction
	// Returns: void, calls callback function
	static void iterateFTPFiles(String parentDir, String currentDir, int level, Callable<Boolean> businessLogicFunction) throws IOException {    
		String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        } 
        Helper.displayInfo("INFO: Current Parent Directory: " + dirToList);
        Helper.displayInfo("INFO: Getting Files for Directory");
        // Get the files for the current directory
        FTPFile[] subFiles = AppProp.ftp.listFiles(dirToList);
        // Ensure there are files
        if (subFiles != null && subFiles.length > 0) {
        	Helper.displayInfo("INFO: Looping Collected Files");
        	// Loop each file
            for (FTPFile aFile : subFiles) {
                AppProp.currentlyProcessingFilename = dirToList + "/" + aFile.getName();   
                Helper.displayInfo("INFO: Current File" + AppProp.currentlyProcessingFilename);
                // If Directory recurse - ensuring business logic of particular folder types
                if (aFile.isDirectory()) {
                	iterateFTPFiles(dirToList, aFile.getName(), level + 1, businessLogicFunction);
                } else {
                	try {
						businessLogicFunction.call();
					} catch (Exception e) {
						
					}
                }
            }
        }
	}
	
	// This method is used to obtain a meta data value or return a blank string
	// Returns: String
	static String getMetaDataValue(Map<String, String> metaData, String val) {
		return metaData.get(val) != null ? metaData.get(val) : "";
	}
	
	// This method is used to wrap XML text inside CDATA at the same time cleanse the text
	static String makeXMLTextSafe(String text) {
		return "<![CDATA[" + text.replace("<", "").replace(">", "").replace("&", "") + "]]>";
	}
}
