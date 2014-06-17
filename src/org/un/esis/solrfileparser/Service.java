package org.un.esis.solrfileparser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;


public class Service {
	
	static int updateCount = 0;
	static int additionCount = 0;
	static int skippedCount = 0;
	static int totalCount = 0;
	
	static BlockingQueue<String> additionsQueue;
	static BlockingQueue<String> deletionsQueue;
	static String[] judgesToExclude;
	
	public static void main(String[] args) {

		/*if (args[0] == null) {
			System.out.println("Please enter the required application type ['OAJ', 'ODS']");
	        System.exit(0);
		}*/
		
		AppProp.appType = "ODS";//getAppType(args[0]);
		
		Helper.displayOS();
		Helper.initialiseConfigFile();
		
		additionsQueue = new ArrayBlockingQueue<>(1000000);
		deletionsQueue = new ArrayBlockingQueue<>(1000000);
		
		// BUILD AppProps File
		AppProp.log = Logger.getLogger(Service.class);
    	Helper.setRunningMachine();
		AppProp.currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		AppProp.outputDatabase = new OutputPostgres();
		AppProp.outputSolrFileAdditions = new OutputSolrFile();
		AppProp.outputSolrFileDeletions = new OutputSolrFile();
		
		AppProp.dataUriDirectory = Helper.getProperty("dataUriDirectory");
		AppProp.systemSeperator = Helper.getProperty("systemSeperator");
		AppProp.outputFileDir = Helper.getProperty("outputDir");
		AppProp.postgresUser = Helper.getProperty("postgresUser");
		AppProp.postgresPassword = Helper.getProperty("postgresPassword");
		AppProp.postgresLocation = Helper.getProperty("postgresLocation");
		AppProp.solrJarFile = Helper.getProperty("solrJarFile");
		AppProp.solrCollection = Helper.getProperty("solrCollection");
		AppProp.debug = Boolean.parseBoolean(Helper.getProperty("debug"));
		
		Helper.displayInfo("INFO: Starting job: ");
		AppProp.log.info("Starting job: " + AppProp.currentDate);
		
    	AppProp.outputDatabase.establishConnection();
		
		if (AppProp.outputDatabase.isConnected()) {
			if (AppProp.appType.equals("OAJ")) {

		    	AppProp.ftp = new FTPClient();
				AppProp.errorTableName = "OAJFileError";
		    	AppProp.tableName = "OAJFile";
		    	Helper.setCurrentOutputFilename(0, true);

		    	AppProp.ftpServerAddress = Helper.getProperty("ftpServerAddress");
		    	AppProp.ftpUserId = Helper.getProperty("ftpUserId");
		    	AppProp.ftpPassword = Helper.getProperty("ftpPassword");
		    	
				String fullJudgesToExclude = Helper.getProperty("judgesToExclude");
				judgesToExclude = (fullJudgesToExclude.length() > 1) ? fullJudgesToExclude.split(",") : null;    	
		    	
		    	processOAJFiles(judgesToExclude);
			}
			
			if (AppProp.appType.equals("ODS")) {

				AppProp.errorTableName = "ODSFileError";
				AppProp.tableName = "ODSFile";
				Helper.setCurrentOutputFilename(0, true);
				
				AppProp.xmlMetaFilename = Helper.getProperty("xmlMetaFilename");
		    	AppProp.contentFilenameStartIdentifer = Helper.getProperty("contentFilenameStartIdentifer");
		    	AppProp.nodeIdentifier = Helper.getProperty("nodeIdentifier");
		    	AppProp.nodeIdentifierParent = Helper.getProperty("nodeIdentifierParent");
		    	AppProp.totalCountPerFile = Integer.parseInt(Helper.getProperty("totalCountPerFile"));
				
				processODSFiles();
			}
		}
		else {
			System.out.println("ERROR: No connection could be established to the Database");
			AppProp.log.info("ERROR: No connection could be established to the Database " + AppProp.currentDate);
		}
	}
	
	static void processODSFiles()
	{
		try {
			IProducer producer = new ProducerODS(additionsQueue, deletionsQueue);
	        new Thread(producer).start();
	        //IConsumer consumer = new ConsumerODS(additionsQueue, deletionsQueue);
	        //new Thread(consumer).start();
		} catch (Exception e) {
			Helper.recordError("Initialising FTP connection (Server:" + AppProp.ftpServerAddress + ") - " + e.getMessage());
		}
	}

	
	static void processOAJFiles(String[] judgesToExclude)
	{
		try {
			IProducer producer = new ProducerOAJ(additionsQueue, deletionsQueue, judgesToExclude);
	    	new Thread(producer).start();
		} catch (Exception e) {
			Helper.recordError("Initialising FTP connection (Server:" + AppProp.ftpServerAddress + ") - " + e.getMessage());
		}
	}
	
	
	static String getAppType(String arg)
	{
		String upperArg = arg.toUpperCase();
		if (upperArg.equals("OAJ"))
			return "OAJ";
		else if (upperArg.equals("ODS"))
			return "ODS";
		else {
			System.out.println("Unknown application type [Not either 'OAJ' nor 'ODS']");
			System.exit(0);
		}
		return null;
	}
}
