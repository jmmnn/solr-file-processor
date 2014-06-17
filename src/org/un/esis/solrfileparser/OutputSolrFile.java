package org.un.esis.solrfileparser;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

//Author: Kevin Bradley
//Date: 11-June-2014
//Description: This class is used to write to the Solr output file
//Version: 1.0
//Code Reviewer:
public class OutputSolrFile implements IFileOutput {
	
	// This method is used to write to the Solr Output file
	// Returns: void
	public void writeOutput(String filename, String xml) {
		
		synchronized(this){
			BufferedWriter outputStream = null;
			FileOutputStream fileOutputStream = null;
			
			try {
				try {
					fileOutputStream = new FileOutputStream(filename, true);
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
					outputStream = new BufferedWriter(outputStreamWriter);
				} catch (FileNotFoundException ef) {
					Helper.recordError("ERROR: Output file not found (" + AppProp.outputFilenameAdditions + "): " + ef.getMessage()); 
				} catch (Exception e) {
					Helper.recordError("ERROR: Writing to output file (" + AppProp.outputFilenameAdditions + "): " + e.getMessage()); 
				}
				outputStream.write(xml);
				
			} catch (Exception e) {
				Helper.recordError("ERROR: Writing to output file using .write (" + AppProp.outputFilenameAdditions + "): " + e.getMessage()); 
			} finally {
				try {
					outputStream.close();
					fileOutputStream.close();
				} catch (IOException e) {
					Helper.recordError("ERROR: Closing output streams (" + AppProp.outputFilenameAdditions + "): " + e.getMessage()); 
				}
			}
	    }
	}
}
