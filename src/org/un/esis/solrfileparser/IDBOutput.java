package org.un.esis.solrfileparser;

import java.sql.Timestamp;
import java.util.Map;

public interface IDBOutput {
	public boolean isConnected();
	public void establishConnection();
	public boolean insertRecord(String title, String filename, String type, Timestamp lastUpdated);
	public boolean logError(String errorMessage);
	public void runQuery(String query, Map<Integer, Object> params) ;
	public boolean getExists(String param);
	public Timestamp getLastUpdated(String param);
	public <T> T callFunction(String functionName, String param, Class<T> type);
	public Object genericCallFunction(String functionName, String param, int outputParamType, Object returnParam);
}
