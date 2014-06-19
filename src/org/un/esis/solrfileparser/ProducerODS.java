package org.un.esis.solrfileparser;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ProducerODS extends DefaultHandler implements IProducer {
 
	static int processedCount = 0;
	BlockingQueue<String> additionsQueue;
	BlockingQueue<String> deletionsQueue;
	MetaFileODS metaFile;
  
    public ProducerODS(BlockingQueue<String> additionsQueue, BlockingQueue<String> deletionsQueue){
    	this.additionsQueue = additionsQueue;
    	this.deletionsQueue = deletionsQueue;
    }

    @Override
    public void run() {
    	applyBusinessLogic();
    }
    
	@Override
	public boolean applyBusinessLogic() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
        //factory.setNamespaceAware( true);
        //factory.setValidating(true);
        try {
        	InputStream inputStream = new FileInputStream(AppProp.xmlMetaFilename);
    		Reader reader = new InputStreamReader(inputStream,"UTF-8");
    		InputSource is = new InputSource(reader);
    		is.setEncoding("UTF-8");
        	SAXParser parser = factory.newSAXParser();
            parser.parse(is, this);
            
        } catch (SAXException e) {
        	Helper.recordError("ERROR: SAXException parsing xmlMetaFile[" + AppProp.xmlMetaFilename + "]: " + e.getMessage());
        } catch (Exception e) {
        	Helper.recordError("ERROR: General exception parsing xmlMetaFile[" + AppProp.xmlMetaFilename + "]: " + e.getMessage());
        }
        return true;
	}
    
    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {
        if (elementName.equalsIgnoreCase(AppProp.nodeIdentifier)) {
        	addValueModelODS(attributes);
        }
        else if (elementName.equalsIgnoreCase(AppProp.nodeIdentifierParent)) {
        	metaFile = new MetaFileODS();
        }
    }
    
	public void error(SAXParseException ex) throws SAXException {
		Helper.recordError("ERROR: SAX Parser exception [" + AppProp.xmlMetaFilename + "]: at line [" + ex.getLineNumber() +"] " + ex.getMessage());
	}
	public void fatalError(SAXParseException ex) throws SAXException {
		Helper.recordError("ERROR: FATAL SAX Parser exception [" + AppProp.xmlMetaFilename + "]: at line [" + ex.getLineNumber() +"] " + ex.getMessage());
	}
    
    @SuppressWarnings("unchecked")
	@Override
    public void endElement(String s, String s1, String elementName) throws SAXException {
        if (elementName.equals(AppProp.nodeIdentifierParent)) {
        	if (metaFile != null) {
        		Map<String, Object> allData = Helper.extractDataViaShare(metaFile.getContentFile());
				Map<String, String> metaData = null;
				String fullText = "";
				
				// Ensure the data is not null
				if (allData != null) {
					Helper.displayInfo("INFO: Setting FullText and MetaData KV Pairs");
					// Extract the fullText and metaData from the Map<String, Object>
					fullText = allData.get("fullText").toString();
					metaData = (Map<String, String>) allData.get("metaData");
					// Cleanse the fullText ensuring no XML escape characters
					fullText = fullText.replace("<", "").replace(">", "");
					metaFile.setFullText(fullText);
					metaFile.setMetaData(metaData);
				
					System.out.println("Add file to queue: " + metaFile.getContentFile() + " - " + AppProp.currentDate);
					Helper.displayInfo("INFO: Mapping File");
					// Map the file - and return the associates SolrXML
	                FileMapping.mapFileODS(metaFile, additionsQueue, deletionsQueue);
	                Helper.displayInfo("INFO: Finished Mapping File");
	                Helper.displayInfo("INFO: Adding Addition File to Queue");
	                processedCount += 1;
				}
        	}
        }
    }
    
    @Override
    public void endDocument() throws SAXException {
        Helper.displayInfo("INFO: Finished adding files to queues");
        IConsumer consumer = new ConsumerODS(additionsQueue, deletionsQueue);
        new Thread(consumer).start();
    }
    
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        new String(ac, i, j);
    }

    private void addValueModelODS(Attributes attributes) {
    	try {
    		String attribName = attributes.getValue("name").toLowerCase();
    		String attribContent = attributes.getValue("content");
    		if (attribName.startsWith("mimetype")) {
	    		metaFile.setMimeType(attribContent);
	    	}
	    	if (attribName.startsWith("documentsymbol")) {
	    		metaFile.addDocumentSymbol(attribName, attribContent);
	    	}
	    	if (attribName.equals("title")) {
	    		metaFile.setTitle(attribContent);
	    		metaFile.setId("ID");
	    	}
	    	if (attribName.equals("size")) {
	    		metaFile.setSize(attribContent);
	    	}
	    	if (attribName.startsWith("agenda")) {
	    		metaFile.addAgenda(attribName, attribContent);
	    	}
	    	if (attribName.startsWith("session")) {
	    		metaFile.addSession(attribName, attribContent);
	    	}
	    	if (attribName.equals("url_job")) {
	    		String url_job = attribContent;
	    		metaFile.setContentFile(AppProp.dataUriDirectory + HelperODS.getContentFilenameCleaned(url_job));
	    		metaFile.setUrlJob(url_job);
	    	}
	    	if (attribName.equals("subject")) {
	    		metaFile.setSubject(attribContent);
	    	}
	    	if (attribName.equals("publicationdate")) {
	    		metaFile.setPublicationDate(attribContent);
	    	}
	    	if (attribName.equals("url")) {
	    		String sUrl = attribContent;
	    		metaFile.setUrl(sUrl);
	    		switch (sUrl.substring(sUrl.length()-6, sUrl.length()).toLowerCase()) {
	    			case "lang=e": 	metaFile.setLanguageCode("en");
	    					 		break;
	    			case "lang=f": 	metaFile.setLanguageCode("fr");
					 			 	break;
	    			case "lang=s": 	metaFile.setLanguageCode("es");
					 		     	break;
	    			case "lang=a": 	metaFile.setLanguageCode("ar");
			 		     			break;
	    			case "lang=r": 	metaFile.setLanguageCode("ru");
			 		     			break;
	    			case "lang=c": 	metaFile.setLanguageCode("zh-cn");
			 		     			break;
	    			default: 		metaFile.setLanguageCode("en");
	    							break;
	    		}
	    	}
    	}
    	catch (Exception e) {
    		Helper.recordError("ERROR: General Error at addValueModelODS: " + e.getMessage());
    	}
    }
}