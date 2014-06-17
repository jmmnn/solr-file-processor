package org.un.esis.solrfileparser;

import java.util.HashMap;
import java.util.Map;

//Author: Kevin Bradley
//Date: 11-June-2014
//Description: This is the meta file for OAJ used to store a pre-processed documents information
//Version: 1.0
//Code Reviewer:
public class MetaFileOAJ extends MetaFile {

	private String filename;
	private String type;
	private String subtype;
	private String fullText;
	private Map<String, String> metaData;
	private String[] judgesToExclude;
	private String languageCode;
	
	public MetaFileOAJ()
	{
		this.metaData = new HashMap<String, String>();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public Map<String, String> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

	public String[] getJudgesToExclude() {
		return judgesToExclude;
	}

	public void setJudgesToExclude(String[] judgesToExclude) {
		this.judgesToExclude = judgesToExclude;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	
	
}
