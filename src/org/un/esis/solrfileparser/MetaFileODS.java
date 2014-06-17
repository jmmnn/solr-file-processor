package org.un.esis.solrfileparser;

import java.util.HashMap;
import java.util.Map;

//Author: Kevin Bradley
//Date: 11-June-2014
//Description: This is the meta file for ODS used to store a pre-processed documents information
//Version: 1.0
//Code Reviewer:
public class MetaFileODS extends MetaFile {
	
	private String Id;
	private String languageCode;
	private String dateCreated;
	private String url;
	private String mimeType;
	
	private String contentFile;
	private Map<String, String> documentSymbols;
	private String title;
	private String size;
	private Map<String, String> agendas;
	private Map<String, String> sessions;
	private String urlJob;
	private String subject;
	private String publicationDate;
	private String fullText;
	private Map<String, String> metaData;
	
	public MetaFileODS()
	{
		this.documentSymbols = new HashMap<String, String>();
		this.agendas = new HashMap<String, String>();
		this.sessions = new HashMap<String, String>();
	}
	
	
	
	public String getContentFile() {
		return contentFile;
	}



	public void setContentFile(String contentFile) {
		this.contentFile = contentFile;
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



	public Map<String, String> getDocumentSymbols() {
		return documentSymbols;
	}
	public void setDocumentSymbols(Map<String, String> documentSymbols) {
		this.documentSymbols = documentSymbols;
	}
	public void addDocumentSymbol(String key, String value)
	{
		this.documentSymbols.put(key, value);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Map<String, String> getAgendas() {
		return agendas;
	}
	public void setAgendas(Map<String, String> agendas) {
		this.agendas = agendas;
	}
	public void addAgenda(String key, String value)
	{
		this.agendas.put(key, value);
	}
	public Map<String, String> getSessions() {
		return sessions;
	}
	public void setSessions(Map<String, String> sessions) {
		this.sessions = sessions;
	}
	public void addSession(String key, String value)
	{
		this.sessions.put(key, value);
	}
	public String getUrlJob() {
		return urlJob;
	}
	public void setUrlJob(String urlJob) {
		this.urlJob = urlJob;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	
}
