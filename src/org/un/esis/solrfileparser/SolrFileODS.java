package org.un.esis.solrfileparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolrFileODS {
	
	protected String id;
	protected String symbol;
	protected String alternativeSymbols;
	protected String title;
	protected String docType;
	protected String size;
	protected Map<String, String> sessions;
	protected Map<String, String> agendas;
	protected String urlJob;
	protected String subjects;
	protected String publicationDate;
	protected String url;
	protected String languageCode;
	protected String body;
	protected String pdfContentLength;
	protected String pdfContentType;
	protected String pdfCreationDate;
	protected String pdfLastModified;
	protected String pdfLastSaveDate;
	protected String pdfCreated;
	protected String pdfDate;
	protected String pdfDCTermsCreated;
	protected String pdfDCTermsModified;
	protected String pdfMetaCreationDate;
	protected String pdfMetaSaveDate;
	protected String pdfModified;
	protected String pdfProducer;
	protected String pdfXMPCreatorTool;
	protected String pdfXMPTpgNPages;
	protected String dateCreated;
	
	protected List<String> refSEC;
	protected List<String> refGA;
	protected List<String> refESC;
	protected List<String> refSC;
	protected List<String> refTC;
	protected List<String> refUNAT;
	protected List<String> refCERD;
	protected List<String> refDP;
	protected List<String> refID;
	protected List<String> refTD;
	protected List<String> refUNEP;
	protected List<String> refUNITAR;
	protected List<String> refWFC;
	
	protected List<String>refC1;
	protected List<String>refC2;
	protected List<String>refC3;
	protected List<String>refC4;
	protected List<String>refC5;
	protected List<String>refC6;
	protected List<String>refSTIC;
	protected List<String>refSTAI;
	protected List<String>refSTSGB;
	protected List<String>refLC;
	protected List<String>refECE;
	protected List<String>refEECA;
	protected List<String>refEESCAP;
	protected List<String>refEESCWA;
	protected List<String>refAHRC;
	protected List<String>refAPLC;
	
	public SolrFileODS()
	{
		this.sessions = new HashMap<String, String>();
		this.agendas = new HashMap<String, String>();
		this.refSEC = new ArrayList<String>();
		this.refGA = new ArrayList<String>();
		this.refESC = new ArrayList<String>();
		this.refSC = new ArrayList<String>();
		this.refTC = new ArrayList<String>();
		this.refUNAT = new ArrayList<String>();
		this.refCERD = new ArrayList<String>();
		this.refDP = new ArrayList<String>();
		this.refID = new ArrayList<String>();
		this.refTD = new ArrayList<String>();
		this.refUNEP = new ArrayList<String>();
		this.refUNITAR = new ArrayList<String>();
		this.refWFC = new ArrayList<String>();
		
		this.refC1 = new ArrayList<String>();
		this.refC2 = new ArrayList<String>();
		this.refC3 = new ArrayList<String>();
		this.refSC = new ArrayList<String>();
		this.refC4 = new ArrayList<String>();
		this.refC5 = new ArrayList<String>();
		this.refC6 = new ArrayList<String>();
		this.refSTIC = new ArrayList<String>();
		this.refSTAI = new ArrayList<String>();
		this.refSTSGB = new ArrayList<String>();
		this.refLC = new ArrayList<String>();
		this.refECE = new ArrayList<String>();
		this.refEECA = new ArrayList<String>();
		this.refEESCAP = new ArrayList<String>();
		this.refEESCWA = new ArrayList<String>();
		this.refAHRC = new ArrayList<String>();
		this.refAPLC = new ArrayList<String>();
	}
	
	
	
	public String getDateCreated() {
		return dateCreated;
	}



	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}



	public List<String> getRefSTAI() {
		return refSTAI;
	}



	public void setRefSTAI(List<String> refSTAI) {
		this.refSTAI = refSTAI;
	}



	public List<String> getRefC1() {
		return refC1;
	}



	public void setRefC1(List<String> refC1) {
		this.refC1 = refC1;
	}



	public List<String> getRefC2() {
		return refC2;
	}



	public void setRefC2(List<String> refC2) {
		this.refC2 = refC2;
	}



	public List<String> getRefC3() {
		return refC3;
	}



	public void setRefC3(List<String> refC3) {
		this.refC3 = refC3;
	}



	public List<String> getRefC4() {
		return refC4;
	}



	public void setRefC4(List<String> refC4) {
		this.refC4 = refC4;
	}



	public List<String> getRefC5() {
		return refC5;
	}



	public void setRefC5(List<String> refC5) {
		this.refC5 = refC5;
	}



	public List<String> getRefC6() {
		return refC6;
	}



	public void setRefC6(List<String> refC6) {
		this.refC6 = refC6;
	}



	public List<String> getRefSTIC() {
		return refSTIC;
	}



	public void setRefSTIC(List<String> refSTIC) {
		this.refSTIC = refSTIC;
	}



	public List<String> getRefSTSGB() {
		return refSTSGB;
	}



	public void setRefSTSGB(List<String> refSTSGB) {
		this.refSTSGB = refSTSGB;
	}



	public List<String> getRefLC() {
		return refLC;
	}



	public void setRefLC(List<String> refLC) {
		this.refLC = refLC;
	}



	public List<String> getRefECE() {
		return refECE;
	}



	public void setRefECE(List<String> refECE) {
		this.refECE = refECE;
	}



	public List<String> getRefEECA() {
		return refEECA;
	}



	public void setRefEECA(List<String> refEECA) {
		this.refEECA = refEECA;
	}



	public List<String> getRefEESCAP() {
		return refEESCAP;
	}



	public void setRefEESCAP(List<String> refEESCAP) {
		this.refEESCAP = refEESCAP;
	}



	public List<String> getRefEESCWA() {
		return refEESCWA;
	}



	public void setRefEESCWA(List<String> refEESCWA) {
		this.refEESCWA = refEESCWA;
	}



	public List<String> getRefAHRC() {
		return refAHRC;
	}



	public void setRefAHRC(List<String> refAHRC) {
		this.refAHRC = refAHRC;
	}



	public List<String> getRefAPLC() {
		return refAPLC;
	}



	public void setRefAPLC(List<String> refAPLC) {
		this.refAPLC = refAPLC;
	}



	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public void addRefGA(String value){
		this.refGA.add(value);
	}

	public List<String> getRefGA() {
		return refGA;
	}

	public void setRefGA(List<String> refGA) {
		this.refGA = refGA;
	}

	public void addRefESC(String value){
		this.refESC.add(value);
	}

	public List<String> getRefESC() {
		return refESC;
	}

	public void setRefESC(List<String> refESC) {
		this.refESC = refESC;
	}

	public void addRefSC(String value){
		this.refSC.add(value);
	}

	public List<String> getRefSC() {
		return refSC;
	}

	public void setRefSC(List<String> refSC) {
		this.refSC = refSC;
	}

	public void addRefTC(String value){
		this.refTC.add(value);
	}

	public List<String> getRefTC() {
		return refTC;
	}

	public void setRefTC(List<String> refTC) {
		this.refTC = refTC;
	}

	public void addRefUNAT(String value){
		this.refUNAT.add(value);
	}

	public List<String> getRefUNAT() {
		return refUNAT;
	}

	public void setRefUNAT(List<String> refUNAT) {
		this.refUNAT = refUNAT;
	}

	public void addRefCERD(String value){
		this.refCERD.add(value);
	}

	public List<String> getRefCERD() {
		return refCERD;
	}

	public void setRefCERD(List<String> refCERD) {
		this.refCERD = refCERD;
	}

	public void addRefDP(String value){
		this.refDP.add(value);
	}

	public List<String> getRefDP() {
		return refDP;
	}

	public void setRefDP(List<String> refDP) {
		this.refDP = refDP;
	}

	public void addRefID(String value){
		this.refID.add(value);
	}

	public List<String> getRefID() {
		return refID;
	}

	public void setRefID(List<String> refID) {
		this.refID = refID;
	}

	public void addRefTD(String value){
		this.refTD.add(value);
	}

	public List<String> getRefTD() {
		return refTD;
	}

	public void setRefTD(List<String> refTD) {
		this.refTD = refTD;
	}

	public void addRefUNEP(String value){
		this.refUNEP.add(value);
	}

	public List<String> getRefUNEP() {
		return refUNEP;
	}

	public void setRefUNEP(List<String> refUNEP) {
		this.refUNEP = refUNEP;
	}

	public void addRefUNITAR(String value){
		this.refUNITAR.add(value);
	}

	public List<String> getRefUNITAR() {
		return refUNITAR;
	}

	public void setRefUNITAR(List<String> refUNITAR) {
		this.refUNITAR = refUNITAR;
	}

	public void addRefWFC(String value){
		this.refWFC.add(value);
	}

	public List<String> getRefWFC() {
		return refWFC;
	}

	public void setRefWFC(List<String> refWFC) {
		this.refWFC = refWFC;
	}

	public void addRefSEC(String value){
		this.refSEC.add(value);
	}
	public List<String> getRefSEC() {
		return refSEC;
	}
	public void setRefSEC(List<String> reference) {
		this.refSEC = reference;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getAlternativeSymbols() {
		return alternativeSymbols;
	}
	public void setAlternativeSymbols(String alternativeSymbols) {
		this.alternativeSymbols = alternativeSymbols;
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
	public Map<String, String> getSessions() {
		return sessions;
	}
	public void addSession(String key, String value){
		this.sessions.put(key, value);
	}
	public void setSessions(Map<String, String> sessions) {
		this.sessions = sessions;
	}
	public String getUrlJob() {
		return urlJob;
	}
	public void setUrlJob(String urlJob) {
		this.urlJob = urlJob;
	}
	public String getSubjects() {
		return subjects;
	}
	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}
	public String getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
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
	public Map<String, String> getAgendas() {
		return agendas;
	}
	public void setAgendas(Map<String, String> agendas) {
		this.agendas = agendas;
	}
	public void addAgenda(String key, String value){
		this.agendas.put(key, value);
	}
}
