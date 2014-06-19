package org.un.esis.solrfileparser;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

//Author: Kevin Bradley
//Date: 11-June-2014
//Description: This is the file mapping class used to map the data extracted onto a specific Solr File
//Version: 1.0
//Code Reviewer:
public class FileMapping {
	
	// Local variables to hold the counts
	static int additionCount = 0;
	static int updateCount = 0;
	
	static String mapFileOAJ(MetaFileOAJ metaFile, BlockingQueue<String> additionsQueue, BlockingQueue<String> deletionsQueue) {
    	String solrXML = "";
    	try {
    		String currentFilename = metaFile.getFilename();
	    	// Create a new Solr File
	    	Helper.displayInfo("INFO: Creating new Solr File for [Filename:" + currentFilename + "]");
	    	SolrFileOAJ sFile = new SolrFileOAJ();

			String language = metaFile.getLanguageCode();
			// Get the filename which includes the extension type i.e. .pdf
			String fnamefull = currentFilename.substring(currentFilename.lastIndexOf(AppProp.systemSeperator)+1);
			// Get the filename exlcuding the .pdf
			String fname = fnamefull.replace(".pdf", "");
			Helper.displayInfo("INFO: Filename: " + fname);
						
			sFile.setId(fname);
			sFile.setFilename(fnamefull);
			sFile.setType(metaFile.getType());
			sFile.setSubtype(metaFile.getSubtype());
			sFile.setTitle(fnamefull);
			// Get the URL of the actual file removing any system seperators - so it can be used on Windows and UNIX
			sFile.setUrl((AppProp.dataUriDirectory + "" + currentFilename).replace(AppProp.systemSeperator, "/"));
			
			// Get the court date - this various custom methods to extract the court date from the full text - parsing and converting it to its english equivelant
			String engDate = HelperOAJ.getEnglishVersionOfDate(Helper.getSingleValue(metaFile.getFullText(), "date\\s*(of judgment)*\\s*:\\s*.+", new String[] { "date", "of judgment", ":" } ));
			sFile.setCourtDate(Helper.getFormattedDate(engDate, "dd MMMMM yyyy"));
			
			String[] judges = null;
			String[] unatJudges = null;
			String[] undtJudges = null;
			String registry = "";
			
			// Perform business logic depending on the language
			Helper.displayInfo("INFO: Performing extraction business logic");
			if (language.equals("fr")) {
				if (metaFile.getType().equals("Dispute Tribunal")) {
					// If its a DT and language is french search for certain keywords to obtain the judges
					judges = Helper.getMultiValue(metaFile.getFullText(), "devant", "greffe", "juge", new String[] { ",", ".", ":" });
					undtJudges = judges;
					registry = "UNDT - " + Helper.getSingleValue(metaFile.getFullText(), "greffe\\s*:\\s*.+", new String[] { "greffe", ":" } ).toUpperCase().replace("È", "E");
				}
				else if (metaFile.getType().equals("Appeals Tribunal")) {
					if (metaFile.getSubtype().equals("Judgment")) {
						// If its a AT, subtype is Judgment and language is french search for certain keywords to obtain the judges
						judges = Helper.getMultiValue(metaFile.getFullText(), "devant", "affaire no", "juge", new String[] { "président", ",", ".", ":" });
						registry = "UNAT - NEW YORK";
					}
					else if (metaFile.getSubtype().equals("Order")) {
						// If its a AT, subtype is Order and language is french search for certain keywords to obtain the judges
						judges = Helper.getMultiValue(metaFile.getFullText(), "devant", "arrêt no", "juge", new String[] { "président", ",", ".", ":" });
						registry = "UNAT - NEW YORK";
					}
					unatJudges = judges;
				}
			}
			else if (language.equals("en")) {
				if (metaFile.getType().equals("Dispute Tribunal")) {
					// If its a DT and language is English search for certain keywords to obtain the judges
					judges = Helper.getMultiValue(metaFile.getFullText(), "before", "registry", "judge", new String[] { ",", ".", ":" });
					undtJudges = judges;
					registry = "UNDT - " + Helper.getSingleValue(metaFile.getFullText(), "registry\\s*:\\s*.+", new String[] { "registry", ":" } ).toUpperCase().replace("È", "E");
				}
				else if (metaFile.getType().equals("Appeals Tribunal")) {
					// If its a DT, subtype is Judgment and language is English search for certain keywords to obtain the judges
					if (metaFile.getSubtype().equals("Judgment")) {
						judges = Helper.getMultiValue(metaFile.getFullText(), "before", "(case no|judgment no|cases no)", "judge", new String[] { "presiding", ",", ".", ":" });
						registry = "UNAT - NEW YORK";
					}
					else if (metaFile.getSubtype().equals("Order")) {
						// If its a DT, subtype is Order and language is English search for certain keywords to obtain the judges
						judges = Helper.getMultiValue(metaFile.getFullText(), "before", "order no", "judge", new String[] { "presiding", ",", ".", ":" });
						registry = "UNAT - NEW YORK";
					}
					unatJudges = judges;
				}
			}	
			// Loop through each of the stores Judges and add these - parsing, cleaning and altering the text into the judges array
			for (int i=0; i < judges.length; i++) {
				String currentJudge = judges[i].trim();
				Boolean foundFaulty = false;
				if (!currentJudge.equals("") && currentJudge.length() < 40) {
					String j = Helper.toCamelCase(HelperOAJ.cleanJudge(judges[i].trim()));
					for (String judge: metaFile.getJudgesToExclude()) {
						if (j.equals(judge)) {
							sFile.addJudge("Judge"+i, "NOT EXTRACTED");
							foundFaulty = true;
							break;
						}
					}
					if (j.equals("Roca"))
						j = "Weinberg de Roca";
					if (!foundFaulty)
						sFile.addJudge("Judge"+i, (j.length() > 1 ? "Judge " + j : "NOT EXTRACTED"));
				}
			}
			// Loop through each of the stores UNDT Judges and add these - parsing, cleaning and altering the text into the judges array
			if (undtJudges != null) {
				for (int i=0; i < undtJudges.length; i++) {
					String currentJudge = undtJudges[i].trim();
					Boolean foundFaulty = false;
					if (!currentJudge.equals("") && currentJudge.length() < 40) {
						String j = Helper.toCamelCase(HelperOAJ.cleanJudge(undtJudges[i].trim()));
						for (String judge: metaFile.getJudgesToExclude()) {
							if (j.equals(judge)) {
								sFile.addJudge("UNDTJudge"+i, "NOT EXTRACTED");
								foundFaulty = true;
								break;
							}
						}
						if (j.equals("Roca"))
							j = "Weinberg de Roca";
						if (!foundFaulty)
							sFile.addJudge("UNDTJudge"+i, (j.length() > 1 ? "Judge " + j : "NOT EXTRACTED"));
					}
				}
			}
			// Loop through each of the stores UNAT Judges and add these - parsing, cleaning and altering the text into the judges array
			if (unatJudges != null) {
				for (int i=0; i < unatJudges.length; i++) {
					String currentJudge = unatJudges[i].trim();
					Boolean foundFaulty = false;
					if (!currentJudge.equals("") && currentJudge.length() < 40) {
						String j = Helper.toCamelCase(HelperOAJ.cleanJudge(unatJudges[i].trim()));
						for (String judge: metaFile.getJudgesToExclude()) {
							if (j.equals(judge)) {
								sFile.addJudge("UNATJudge"+i, "NOT EXTRACTED");
								foundFaulty = true;
								break;
							}
						}
						if (j.equals("Roca"))
							j = "Weinberg de Roca";
						if (!foundFaulty)
							sFile.addJudge("UNATJudge"+i, (j.length() > 1 ? "Judge " + j : "NOT EXTRACTED"));					
					}
				}
			}
			// Set the Extracted Meta Data values into the Solr Object
			sFile.setLastModified(
					Helper.getMetaDataValue(metaFile.getMetaData(), "Last-Modified").isEmpty() ? "1900-01-01T00:00:00Z" : Helper.getMetaDataValue(metaFile.getMetaData(), "Last-Modified")
			);
			sFile.setMimeType(Helper.getMetaDataValue(metaFile.getMetaData(), "Content-Type"));
			sFile.setRegistry(HelperOAJ.convertRegistry(registry));
			sFile.setPdfDCTermsModified(Helper.getMetaDataValue(metaFile.getMetaData(), "dcterms:modified"));
			sFile.setPdfDCTermsCreated(Helper.getMetaDataValue(metaFile.getMetaData(), "dcterms:created"));
			sFile.setPdfContentType(Helper.getMetaDataValue(metaFile.getMetaData(), "Content-Type"));
			sFile.setPdfCreationDate(Helper.getMetaDataValue(metaFile.getMetaData(), "Creation-Date"));
			sFile.setPdfMetaCreationDate(Helper.getMetaDataValue(metaFile.getMetaData(), "meta:creation-date"));
			sFile.setPdfMetaSaveDate(Helper.getMetaDataValue(metaFile.getMetaData(), "meta:save-date"));
			sFile.setPdfLastModified(Helper.getMetaDataValue(metaFile.getMetaData(), "Last-Modified"));
			sFile.setPdfLastSaveDate(Helper.getMetaDataValue(metaFile.getMetaData(), "Last-Save-Date"));
			sFile.setPdfXMPCreatorTool(Helper.makeXMLTextSafe(Helper.getMetaDataValue(metaFile.getMetaData(), "xmp:CreatorTool")));
			sFile.setPdfCreated(Helper.getMetaDataValue(metaFile.getMetaData(), "created"));
			sFile.setPdfDate(Helper.getMetaDataValue(metaFile.getMetaData(), "date"));
			sFile.setPdfXMPTpgNPages(Helper.getMetaDataValue(metaFile.getMetaData(), "xmpTPg:NPages"));
			sFile.setPdfModified(Helper.getMetaDataValue(metaFile.getMetaData(), "modified"));
			sFile.setPdfProducer(Helper.makeXMLTextSafe(Helper.getMetaDataValue(metaFile.getMetaData(), "producer")));
			sFile.setPdfContentLength(metaFile.getFullText() != null ? Integer.toString(metaFile.getFullText().length()) : "0");	
			sFile.setBody(Helper.makeXMLTextSafe(metaFile.getFullText()));
			sFile.setDateCreated(
					Helper.getMetaDataValue(metaFile.getMetaData(), "meta:creation-date").isEmpty() ? "1900-01-01T00:00:00Z" : Helper.getMetaDataValue(metaFile.getMetaData(), "meta:creation-date")
					);
			Helper.displayInfo("INFO: Extracted all data now building SolrXML");
			solrXML = constructOAJSolrXML(sFile);
						
			// Identify whether a file exists or not based on its filename/URL
			Helper.displayInfo("INFO: Checking if file exists");
			Boolean fileExits = AppProp.outputDatabase.getExists(currentFilename);
			
			String lastUpdated = Helper.getMetaDataValue(metaFile.getMetaData(), "Last-Modified");
			Timestamp currentTimestamp = Helper.getTimestamp(lastUpdated);
			
	        if (fileExits) {
	        	Timestamp lastUpdatedString = AppProp.outputDatabase.getLastUpdated(currentFilename);
	        	String date = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(lastUpdatedString);
	        	
	        	if (lastUpdated.equals("") || lastUpdated == null)
	        		//lastUpdated  = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format("1900-01-01T00:00:00Z");
	        		lastUpdated = "1900-01-01T00:00:00Z";
	        	if (!date.equals(lastUpdated)) {
	        		Helper.displayInfo("INFO: File has been updated...");
		        	// If the file exists and the last updated does NOT equal the current last updated, add a DELETE document
		        	// It will remove the existing and re-add the new one
		        	deletionsQueue.add(fname);
		        	additionsQueue.add(solrXML);
		        	updateCount += 1;
		        	AppProp.outputDatabase.insertRecord(fnamefull, currentFilename, "Deletion", currentTimestamp);
		        	AppProp.outputDatabase.insertRecord(fnamefull, currentFilename, "Readdition", currentTimestamp);
	        	}
	        }
	        else
	        {
	        	// If the file doesnt exist add it
	        	AppProp.outputDatabase.insertRecord(fnamefull, currentFilename, "Addition", currentTimestamp);
	            // Add this XML to the additions queue
	            additionsQueue.add(solrXML);
	            additionCount += 1;
	        }
    	} catch (Exception ex)
    	{
    		Helper.recordError("General Error at File Mapping - " + ex.getMessage());
    	}
		
		return solrXML;
	}
	
	@SuppressWarnings("serial")
	static String mapFileODS(MetaFileODS metaFile, BlockingQueue<String> additionsQueue, BlockingQueue<String> deletionsQueue) {
		String solrXML = "";
    	try {
	    	// Create a new Solr File
	    	Helper.displayInfo("INFO: Creating new Solr File for [Filename:" + metaFile.getTitle() + "]");
	    	SolrFileODS sFile = new SolrFileODS();

	    	String languageCode = metaFile.getLanguageCode();
			sFile.setLanguageCode(languageCode);
			sFile.setTitle(Helper.makeXMLTextSafe(metaFile.getTitle()));
			sFile.setSize(metaFile.getSize());
			sFile.setUrl(metaFile.getUrl().replace("&", "&amp;"));
			sFile.setUrlJob(metaFile.getUrlJob());
			sFile.setSubjects(Helper.makeXMLTextSafe(metaFile.getSubject()));
			sFile.setPublicationDate(Helper.getFormattedDate(metaFile.getPublicationDate(), "dd MMM, yyyy"));

			String symbols = "";
			String initialSymbol = "";
			Integer symCount = 0;
			for (Map.Entry<String, String> sym : metaFile.getDocumentSymbols().entrySet()) {
				String symVal = sym.getValue();
				String symKey = sym.getKey();
				if (symKey.equals("documentsymbol1")) {
					initialSymbol = symVal;
				} else {
					symbols += ((symVal.isEmpty()) ? "" : ((symCount > 1) ? ","
							+ symVal : symVal));
				}
				symCount++;
			}
			String cleanedSymbol = initialSymbol.replace("&","");
			String id = cleanedSymbol + "_" + languageCode;
			
			sFile.setSymbol(Helper.makeXMLTextSafe(cleanedSymbol));
			sFile.setAlternativeSymbols(Helper.makeXMLTextSafe(symbols));
			sFile.setId(Helper.makeXMLTextSafe(id));
			
			sFile.setDocType(Helper.makeXMLTextSafe(HelperODS.getDocumentType(cleanedSymbol)));
			
			// KEVIN TESTING REGEX CODE
			List<String> refs = HelperODS.getReferences(metaFile.getFullText());
			if (!refs.isEmpty()) {

				sFile.setRefSEC(HelperODS.getSpecifcReference("ST/", refs, new ArrayList<String>() {{ add("ST/IC"); add("St/AI"); add("ST/SGB"); }} ));
				sFile.setRefGA(HelperODS.getSpecifcReference("A/", refs, new ArrayList<String>() {{ add("A/HRC"); add("A/C.1"); add("A/C.2"); add("A/C.3"); add("A/C.4"); add("A/C.5"); add("A/C.6"); }} ));
				sFile.setRefESC(HelperODS.getSpecifcReference("E/", refs, new ArrayList<String>() {{ add("E/ECA"); add("E/ESCAP"); add("E/ESCAP"); }}));
				sFile.setRefSC(HelperODS.getSpecifcReference("S/", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefTC(HelperODS.getSpecifcReference("T/", refs, new ArrayList<String>() {{ }}));
				sFile.setRefUNAT(HelperODS.getSpecifcReference("AT/", refs, new ArrayList<String>() {{ }}));
				sFile.setRefCERD(HelperODS.getSpecifcReference("CERD/", refs, new ArrayList<String>() {{ }}));
				sFile.setRefDP(HelperODS.getSpecifcReference("DP/", refs, new ArrayList<String>() {{ }}));
				sFile.setRefID(HelperODS.getSpecifcReference("ID/", refs, new ArrayList<String>() {{ }}));
				sFile.setRefTD(HelperODS.getSpecifcReference("TD/", refs, new ArrayList<String>() {{ }}));
				sFile.setRefUNEP(HelperODS.getSpecifcReference("UNEP/", refs, new ArrayList<String>() {{ }}));
				sFile.setRefUNITAR(HelperODS.getSpecifcReference("UNITAR/", refs, new ArrayList<String>() {{ }}));
				sFile.setRefWFC(HelperODS.getSpecifcReference("WFC/", refs, new ArrayList<String>() {{ }}));
				
				sFile.setRefC1(HelperODS.getSpecifcReference("A/C.1", refs, new ArrayList<String>() {{ }}));
				sFile.setRefC2(HelperODS.getSpecifcReference("A/C.2", refs, new ArrayList<String>() {{ }}));
				sFile.setRefC3(HelperODS.getSpecifcReference("A/C.3", refs, new ArrayList<String>() {{ }}));
				sFile.setRefC4(HelperODS.getSpecifcReference("A/C.4", refs, new ArrayList<String>() {{ }}));
				sFile.setRefC5(HelperODS.getSpecifcReference("A/C.5", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefC6(HelperODS.getSpecifcReference("A/C.6", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefSTIC(HelperODS.getSpecifcReference("ST/IC", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefSTAI(HelperODS.getSpecifcReference("ST/AI", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefSTSGB(HelperODS.getSpecifcReference("ST/SGB", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefLC(HelperODS.getSpecifcReference("LC/", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefECE(HelperODS.getSpecifcReference("ECE/", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefEECA(HelperODS.getSpecifcReference("E/ECA", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefEESCAP(HelperODS.getSpecifcReference("E/ESCAP", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefEESCWA(HelperODS.getSpecifcReference("E/ESCWA", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefAHRC(HelperODS.getSpecifcReference("A/HRC", refs, new ArrayList<String>() {{ }} ));
				sFile.setRefAPLC(HelperODS.getSpecifcReference("APLC/", refs, new ArrayList<String>() {{ }} ));
			}

			for (Map.Entry<String, String> ses : metaFile.getSessions().entrySet()) {
				sFile.addSession(ses.getKey(), ses.getValue());
			}

			for (Map.Entry<String, String> agen : metaFile.getAgendas().entrySet()) {
				sFile.addAgenda(agen.getKey(), agen.getValue());
			}
			sFile.setPdfDCTermsModified(Helper.getMetaDataValue(metaFile.getMetaData(), "dcterms:modified"));
			sFile.setPdfDCTermsCreated(Helper.getMetaDataValue(metaFile.getMetaData(), "dcterms:created"));
			sFile.setPdfContentType(Helper.getMetaDataValue(metaFile.getMetaData(), "Content-Type"));
			sFile.setPdfCreationDate(Helper.getMetaDataValue(metaFile.getMetaData(), "Creation-Date"));
			sFile.setPdfMetaCreationDate(Helper.getMetaDataValue(metaFile.getMetaData(), "meta:creation-date"));
			sFile.setPdfMetaSaveDate(Helper.getMetaDataValue(metaFile.getMetaData(), "meta:save-date"));
			sFile.setPdfLastModified(Helper.getMetaDataValue(metaFile.getMetaData(), "Last-Modified"));
			sFile.setPdfLastSaveDate(Helper.getMetaDataValue(metaFile.getMetaData(), "Last-Save-Date"));
			sFile.setPdfXMPCreatorTool(Helper.makeXMLTextSafe(Helper.getMetaDataValue(metaFile.getMetaData(), "xmp:CreatorTool")));
			sFile.setPdfCreated(Helper.getMetaDataValue(metaFile.getMetaData(), "created"));
			sFile.setPdfDate(Helper.getMetaDataValue(metaFile.getMetaData(), "date"));
			sFile.setPdfXMPTpgNPages(Helper.getMetaDataValue(metaFile.getMetaData(), "xmpTPg:NPages"));
			sFile.setPdfModified(Helper.getMetaDataValue(metaFile.getMetaData(), "modified"));
			sFile.setPdfProducer(Helper.makeXMLTextSafe(Helper.getMetaDataValue(metaFile.getMetaData(), "producer")));
			sFile.setPdfContentLength(metaFile.getFullText() != null ? Integer.toString(metaFile.getFullText().length()) : "0");	
			sFile.setDateCreated(Helper.getMetaDataValue(metaFile.getMetaData(), "meta:creation-date"));
			sFile.setBody(Helper.makeXMLTextSafe(metaFile.getFullText()));
			Helper.displayInfo("INFO: Extracted all data now building SolrXML");
			solrXML = constructODSSolrXML(sFile);
						
			// Identify whether a file exists or not based on its filename/URL
			Helper.displayInfo("INFO: Checking if file exists");
			Boolean fileExits = AppProp.outputDatabase.getExists(id);
			
			String lastUpdated = Helper.getMetaDataValue(metaFile.getMetaData(), "Last-Modified");
			Timestamp currentTimestamp = Helper.getTimestamp(lastUpdated);
			
	        if (fileExits) {
	        	Timestamp lastUpdatedString = AppProp.outputDatabase.getLastUpdated(id);
	        	String date = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(lastUpdatedString);
	        	
	        	if (lastUpdated.equals("") || lastUpdated == null)
	        		lastUpdated = "1900-01-01T00:00:00Z";
	        	if (!date.equals(lastUpdated)) {
	        		Helper.displayInfo("INFO: File has been updated...");
		        	// If the file exists and the last updated does NOT equal the current last updated, add a DELETE document
		        	// It will remove the existing and re-add the new one
		        	deletionsQueue.add(id);
		        	additionsQueue.add(solrXML);
		        	updateCount += 1;
		        	AppProp.outputDatabase.insertRecord(metaFile.getTitle(), id, "Deletion", currentTimestamp);
		        	AppProp.outputDatabase.insertRecord(metaFile.getTitle(), id, "Readdition", currentTimestamp);
	        	}
	        }
	        else
	        {
	        	// If the file doesnt exist add it
	        	AppProp.outputDatabase.insertRecord(metaFile.getTitle(), id, "Addition", currentTimestamp);
	            // Add this XML to the additions queue
	            additionsQueue.add(solrXML);
	            additionCount += 1;
	        }
    	} catch (Exception ex)
    	{
    		Helper.recordError("General Error at File Mapping - " + ex.getMessage());
    	}

		return solrXML;
	}
	
    @SuppressWarnings("unchecked")
    static String constructOAJSolrXML(SolrFileOAJ file) {
		try {
			String xml = "<doc>\n";
			for (Field field : file.getClass().getDeclaredFields()) {
				if (field.get(file) != null) {
					if (field.getType().isAssignableFrom(Map.class)) {
						Map<String, String> vals = ((Map<String, String>) field
								.get(file));
						for (Map.Entry<String, String> kv : vals.entrySet()) {
							if (!kv.getValue().isEmpty())
								if (kv.getKey().startsWith("UNDTJudge")) 
									xml += "<field name=\"UNDTJudge\">" + kv.getValue() + "</field>\n";
								else if (kv.getKey().startsWith("UNATJudge")) 
									xml += "<field name=\"UNATJudge\">" + kv.getValue() + "</field>\n";
								else
									xml += "<field name=\"Judge\">" + kv.getValue() + "</field>\n";
						}
					} else {
						xml += "<field name=\"" + field.getName() + "\">"
								+ field.get(file) + "</field>\n";
					}
				}
			}
			xml += "</doc>\n";
			return xml;
		} catch (Exception e) {
			Helper.recordError("General Error at constructOAJSolrXML - " + e.getMessage());
			return "";
		}
	}
    
    @SuppressWarnings("unchecked")
	static String constructODSSolrXML(SolrFileODS file) {
		try {
			String xml = "<doc>\n";
			for (Field field : file.getClass().getDeclaredFields()) {
				if (field.get(file) != null) {
					if (field.getType().isAssignableFrom(Map.class)) {
						Map<String, String> vals = ((Map<String, String>) field
								.get(file));
						for (Map.Entry<String, String> kv : vals.entrySet()) {
							if (!kv.getValue().isEmpty())
								xml += "<field name=\"" + kv.getKey() + "\">"
										+ kv.getValue() + "</field>\n";
						}
					}else if (field.getType().isAssignableFrom(List.class)) {
						List<String> vals = ((ArrayList<String>) field.get(file));
						for (String kv : vals) {
							if (!kv.isEmpty())
								xml += "<field name=\"" + field.getName() + "\">"
										+ kv + "</field>\n";
						}
					} else {
						xml += "<field name=\"" + field.getName() + "\">"
								+ field.get(file) + "</field>\n";
					}
				}
			}
			xml += "</doc>\n";
			return xml;
		} catch (Exception e) {
			Helper.recordError("General Error at constructODSSolrXML - " + e.getMessage());
			return "";
		}
	}
}
