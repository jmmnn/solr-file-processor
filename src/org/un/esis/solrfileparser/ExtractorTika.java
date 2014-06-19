package org.un.esis.solrfileparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream; 
import java.util.HashMap;
import java.util.Map;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;

//Author: Kevin Bradley
//Date: 11-June-2014
//Description: This is a tika wrapper class used to pull all data from a file using Apache Tika project
//Version: 1.0
//Code Reviewer:
public class ExtractorTika {

	// This is the actual extraction method which uses Tika to extract all available data from the file
	// Returns: KV Pair of full text and meta data
	public Map<String, Object> extraction(File file) {
		Map<String, Object> allData = new HashMap<String, Object>();
		Map<String, String> metaData = new HashMap<String, String>();
		
		try {		
			// Open an IS using the file passed in with param
			InputStream is = new FileInputStream(file);
			Metadata metadata = new Metadata();
			BodyContentHandler ch = new BodyContentHandler(1000000000);
			AutoDetectParser parser = new AutoDetectParser();
		
			Helper.displayInfo("INFO: Detecting the MIME Type of the File using Tika " + file.getAbsolutePath());
			// Identify the mime type
			String mimeType = new Tika().detect(file);
			metadata.set(Metadata.CONTENT_TYPE, mimeType);
		
			Helper.displayInfo("INFO: Extracting Data for File using Tika " + file.getAbsolutePath());
			// Use the parser to extract the data
			parser.parse(is, ch, metadata, new ParseContext());
			// Close the is
			is.close();
		
			// Extract the fulltext
			String fullText = ch.toString();
			Helper.displayInfo("INFO: Storing extracted data for " + file.getAbsolutePath());
		  
			// Loop through and obtain the meta data available
			Helper.displayInfo("INFO: Obtaining the meta data");
			for (int i = 0; i < metadata.names().length; i++) {
				String item = metadata.names()[i];
				metaData.put(item, metadata.get(item));
			}
		  
			// Put the data into a KV for return
			allData.put("fullText", fullText);
			allData.put("metaData", metaData);

			return allData;
		}
		catch (FileNotFoundException enf){
			Helper.recordError("ERROR: File not found: (" + file.getAbsolutePath() + ") and unable to extract full text and meta data");
			return null;
		}
		catch (Exception e) {
			Helper.recordError("ERROR: General error in ExtractorTika: " + e.getMessage());
			return null;
		}

	}
}
