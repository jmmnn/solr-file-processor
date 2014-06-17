package org.un.esis.solrfileparser;

import java.net.InetAddress;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

//Author: Kevin Bradley
//Date: 11-June-2014
//Description: This is a container class used to store app wide properties used within the application
//Version: 1.0
//Code Reviewer:
public class AppProp {
	
	static String appType;
	static String currentDate;
	static OutputSolrFile outputSolrFileAdditions;
	static OutputSolrFile outputSolrFileDeletions;
	static InetAddress runningMachine;
	static String OS = System.getProperty("os.name").toLowerCase();
	
	static OutputPostgres outputDatabase;
	static String postgresLocation;
	static String postgresUser;
	static String postgresPassword;
	static String tableName;
	static String errorTableName;
	
	static Logger log;
	static Properties configFile;
	
	static String systemSeperator;
	static String dataUriDirectory;
	static String outputFileDir;
	static String outputFilenameAdditions;
	static String outputFilenameDeletions;
	static String currentlyProcessingFilename;
	
	static FTPClient ftp;
	static String tempTikaFTPFilename = "SolrFileProcessor_DownloadedTempFile.pdf";
	static String ftpServerAddress;
	static String ftpUserId;
	static String ftpPassword;
	
	static String xmlMetaFilename;
	static String contentFilenameStartIdentifer;
	static String nodeIdentifier;
	static String nodeIdentifierParent;
	static int totalCountPerFile;
	
	static String solrJarFile;
	static String solrCollection;
	
	static boolean debug;
}
