package org.un.esis.solrfileparser;

import java.util.HashMap;
import java.util.Map;

public class SolrFileOAJ {

	public String id;
	public String url;
	public String languageCode;
	public String body;
	public String pdfContentLength;
	public String pdfContentType;
	public String pdfCreationDate;
	public String pdfLastModified;
	public String pdfLastSaveDate;
	public String pdfCreated;
	public String pdfDate;
	public String pdfDCTermsCreated;
	public String pdfDCTermsModified;
	public String pdfMetaCreationDate;
	public String pdfMetaSaveDate;
	public String pdfModified;
	public String pdfProducer;
	public String pdfXMPCreatorTool;
	public String pdfXMPTpgNPages;
	public String mimeType;
	public String lastModified;
	public String dateCreated;
	
	protected String title;
	protected String filename;
	protected String type;
	protected String subtype;
	protected String courtDate;
	protected Map<String, String> judges;
	protected String registry;
	protected Map<String, String> undtJudges;
	protected Map<String, String> unatJudges;
	
	public SolrFileOAJ()
	{
		this.judges = new HashMap<String, String>();
		this.undtJudges = new HashMap<String, String>();
		this.unatJudges = new HashMap<String, String>();
	}
	
	public void addJudge(String key, String value){
		this.judges.put(key, value);
	}
	
	public void addUnatJudge(String key, String value){
		this.unatJudges.put(key, value);
	}
	
	public void addUndtJudge(String key, String value){
		this.undtJudges.put(key, value);
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getCourtDate() {
		return courtDate;
	}
	public void setCourtDate(String courtDate) {
		this.courtDate = courtDate;
	}
	public Map<String, String> getJudges() {
		return judges;
	}
	public void setJudges(Map<String, String> judges) {
		this.judges = judges;
	}
	public String getRegistry() {
		return registry;
	}
	public void setRegistry(String registry) {
		this.registry = registry;
	}
	public Map<String, String> getUndtJudges() {
		return undtJudges;
	}
	public void setUndtJudges(Map<String, String> undtJudges) {
		this.undtJudges = undtJudges;
	}
	public Map<String, String> getUnatJudges() {
		return unatJudges;
	}
	public void setUnatJudges(Map<String, String> unatJudges) {
		this.unatJudges = unatJudges;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPdfContentLength() {
		return pdfContentLength;
	}

	public void setPdfContentLength(String pdfContentLength) {
		this.pdfContentLength = pdfContentLength;
	}

	public String getPdfContentType() {
		return pdfContentType;
	}

	public void setPdfContentType(String pdfContentType) {
		this.pdfContentType = pdfContentType;
	}

	public String getPdfCreationDate() {
		return pdfCreationDate;
	}

	public void setPdfCreationDate(String pdfCreationDate) {
		this.pdfCreationDate = pdfCreationDate;
	}

	public String getPdfLastModified() {
		return pdfLastModified;
	}

	public void setPdfLastModified(String pdfLastModified) {
		this.pdfLastModified = pdfLastModified;
	}

	public String getPdfLastSaveDate() {
		return pdfLastSaveDate;
	}

	public void setPdfLastSaveDate(String pdfLastSaveDate) {
		this.pdfLastSaveDate = pdfLastSaveDate;
	}

	public String getPdfCreated() {
		return pdfCreated;
	}

	public void setPdfCreated(String pdfCreated) {
		this.pdfCreated = pdfCreated;
	}

	public String getPdfDate() {
		return pdfDate;
	}

	public void setPdfDate(String pdfDate) {
		this.pdfDate = pdfDate;
	}

	public String getPdfDCTermsCreated() {
		return pdfDCTermsCreated;
	}

	public void setPdfDCTermsCreated(String pdfDCTermsCreated) {
		this.pdfDCTermsCreated = pdfDCTermsCreated;
	}

	public String getPdfDCTermsModified() {
		return pdfDCTermsModified;
	}

	public void setPdfDCTermsModified(String pdfDCTermsModified) {
		this.pdfDCTermsModified = pdfDCTermsModified;
	}

	public String getPdfMetaCreationDate() {
		return pdfMetaCreationDate;
	}

	public void setPdfMetaCreationDate(String pdfMetaCreationDate) {
		this.pdfMetaCreationDate = pdfMetaCreationDate;
	}

	public String getPdfMetaSaveDate() {
		return pdfMetaSaveDate;
	}

	public void setPdfMetaSaveDate(String pdfMetaSaveDate) {
		this.pdfMetaSaveDate = pdfMetaSaveDate;
	}

	public String getPdfModified() {
		return pdfModified;
	}

	public void setPdfModified(String pdfModified) {
		this.pdfModified = pdfModified;
	}

	public String getPdfProducer() {
		return pdfProducer;
	}

	public void setPdfProducer(String pdfProducer) {
		this.pdfProducer = pdfProducer;
	}

	public String getPdfXMPCreatorTool() {
		return pdfXMPCreatorTool;
	}

	public void setPdfXMPCreatorTool(String pdfXMPCreatorTool) {
		this.pdfXMPCreatorTool = pdfXMPCreatorTool;
	}

	public String getPdfXMPTpgNPages() {
		return pdfXMPTpgNPages;
	}

	public void setPdfXMPTpgNPages(String pdfXMPTpgNPages) {
		this.pdfXMPTpgNPages = pdfXMPTpgNPages;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	
	
}
