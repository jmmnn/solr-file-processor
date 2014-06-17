package org.un.esis.solrfileparser;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

//Author: Kevin Bradley
//Date: 11-June-2014
//Description: This class is used as an output to PostGres database and implements the IDBOutput interface
//Version: 1.0
//Code Reviewer:
public class OutputPostgres implements IDBOutput {

	private Connection connection = null;
	
	// This method is used to identify if the application is connected to postgres
	// Returns: Boolean
	public boolean isConnected() {
		try {
			if (!connection.isClosed())
				return true;
			else
				return false;
		} catch (SQLException e) {
			Helper.recordError("ERROR: Checking if connected to Postgres: " + e.getMessage());
			return false;
		}
	}
	
	// This method is used to establish a connection to the Postgres Database
	// Returns: void
	public void establishConnection() {
		if (connection == null) {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				Helper.recordError("ERROR: Attempting to obtain Driver org.postgresql.Driver: " + e.getMessage());
			}
			
			try {
				Helper.displayInfo("INFO: Connecting to Database [Server:" + AppProp.postgresLocation + "] [User: " + AppProp.postgresUser + "] [Password: " + AppProp.postgresPassword + "]");
				connection = DriverManager.getConnection("jdbc:postgresql://" + AppProp.postgresLocation, AppProp.postgresUser, AppProp.postgresPassword);
			} catch (SQLException e) {
				Helper.recordError("ERROR: Cannot establish a connection to Postgres " + e.getMessage());
			}
		}
	}
	
	// This method is used to insert a record into the database
	// Returns: Boolean
	public boolean insertRecord(String title, String filename, String type, Timestamp lastUpdated) {
		try {			    
		    String qry = "INSERT INTO \""+ AppProp.tableName + "\" (\"Title\", \"File\", \"Type\", \"LastUpdated\", \"Processed\", \"OutputFile\") VALUES(?, ?, ?, ?, ?, ?)";
		    Map<Integer, Object> params = new HashMap<Integer, Object>();
		    params.put(1, title);
		    params.put(2, filename);
		    params.put(3, type);
		    params.put(4, lastUpdated);
		    params.put(5, Helper.getCurrentTimestamp());
		    params.put(6, AppProp.outputFilenameAdditions);

		    runQuery(qry, params);
			
			return true;
			
		} catch (Exception e) {
			Helper.recordError("ERROR: Inserting record to Postgres table [" + AppProp.tableName + "] " + e.getMessage());
			return false;
		}
	}
	
	// This method is used to log an error to the error table within postgres
	// Returns: Boolean
	public boolean logError(String errorMessage) {
		try {	
		    String qry = "INSERT INTO \""+ AppProp.errorTableName + "\" (\"ErrorMessage\", \"File\", \"OutputFile\", \"Processed\") VALUES(?, ?, ?, ?)";
		    Map<Integer, Object> params = new HashMap<Integer, Object>();
		    params.put(1, errorMessage);
		    params.put(2, AppProp.currentlyProcessingFilename);
		    params.put(3, AppProp.outputFilenameAdditions);
		    params.put(4, Helper.getCurrentTimestamp());

		    runQuery(qry, params);
			
			return true;
			
		} catch (Exception e) {
			Helper.recordError("ERROR: Attempting to write to error log in Postgres table [" + AppProp.errorTableName + "] " + e.getMessage());
			return false;
		}
	}

	// This method is used to run a query on the postgres database assigning parameters
	// Returns: void
	public void runQuery(String query, Map<Integer, Object> params) {
		PreparedStatement pst;
		try {
			pst = this.connection.prepareStatement(query);
			for (Map.Entry<Integer, Object> kv : params.entrySet()) {
				if (kv.getValue().getClass().isAssignableFrom(String.class)) {
					pst.setString(kv.getKey(), (String) kv.getValue());
				}
				if (kv.getValue().getClass().isAssignableFrom(Timestamp.class)) {
					pst.setTimestamp(kv.getKey(), (Timestamp) kv.getValue());
				}	
			}
			pst.executeUpdate();
		} catch (SQLException e) {
			Helper.recordError("ERROR: Attempting to run query (" + query + "): " + e.getMessage());
		}
		
	}

	// This method is used to identify if a record exists based on a parameter
	// Returns: Boolean
	public boolean getExists(String param) {
		Boolean itExists = this.<Boolean>callFunction("recordExists", param, Boolean.class);
		return itExists;
	}
	
	// This method is used to obtain the last updated timestamp of a record within the database
	// Returns: Timestamp
	public Timestamp getLastUpdated(String param) {
		Timestamp ts = this.<Timestamp>callFunction("getLastUpdated", param, Timestamp.class);
		return ts;
	}

	// This method is used to call a postgres function based on a class type
	// Returns: T of type Class
	@SuppressWarnings("unchecked")
	public <T> T callFunction(String functionName, String param, Class<T> type) {
		
		Object returnParam = null;
		
		if (type.isAssignableFrom(Boolean.class)) {
			returnParam = genericCallFunction(functionName, param, Types.BOOLEAN, new Boolean(false));
		}
		if (type.isAssignableFrom(Timestamp.class)) {
			returnParam = genericCallFunction(functionName, param, Types.TIMESTAMP, new Timestamp(0));
		}

		return (T) returnParam;
	}
	
	// This method is a generic method to to call a function within Postgres
	// Returns: Object of param type
	public Object genericCallFunction(String functionName, String param, int outputParamType, Object returnParam) {
			
		try {
			CallableStatement funct = this.connection.prepareCall("{ ? = call " + functionName + "( ? ) }");
			funct.registerOutParameter(1, outputParamType);
			funct.setString(2, param);
			funct.execute();
			if (returnParam instanceof Boolean) {
				returnParam = funct.getBoolean(1);
			}
			if (returnParam instanceof Timestamp) {
				returnParam = funct.getTimestamp(1);
			}
			funct.close();
			return returnParam;
		} catch (SQLException e) {
			Helper.recordError("ERROR: Attempting to call function(" + functionName + "): " + e.getMessage());
		}
		return null;
	}

}
