package org.un.esis.solrfileparser;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class ConsumerODS implements IConsumer {

	BlockingQueue<String> additionsQueue;
	BlockingQueue<String> deletionsQueue;

	public ConsumerODS(BlockingQueue<String> additionsQueue, BlockingQueue<String> deletionsQueue) {
		
		this.additionsQueue = additionsQueue;
		this.deletionsQueue = deletionsQueue;

	}

	public void run() {
		
		int currentOutputFileCount = 1;
		// Ensure there are deletions
    	if (deletionsQueue.size() > 0) {
    		Helper.displayInfo("INFO: Adding DELETION FILES");
    		Helper.setCurrentOutputFilename(0, false);
    		// Initial XML parent node element start
    		AppProp.outputSolrFileDeletions.writeOutput(AppProp.outputFilenameDeletions, "<delete>\n");
	    	String currentSolrFileId = "";
	    	try {
	    		// Loop through each of the deletions and add these to the output file
				while ((currentSolrFileId = deletionsQueue.poll(5, TimeUnit.SECONDS)) != null) {
					Helper.displayInfo("INFO: Writing file to output file [" + AppProp.outputFilenameDeletions + "]");
					AppProp.outputSolrFileDeletions.writeOutput(AppProp.outputFilenameDeletions, "<id>" + currentSolrFileId + "</id>\n");
				}
			} catch (Exception e) {
				Helper.recordError("Outputting Deletion - " + e.getMessage());
			}
	    	// End the parent XML node element
	    	AppProp.outputSolrFileDeletions.writeOutput(AppProp.outputFilenameDeletions, "</delete>\n");
	    	Helper.displayInfo("INFO: Finished Adding DELETION FILES");
	    	Helper.postDataToSolr(AppProp.outputFilenameDeletions);
    	}
    	
    	if (additionsQueue.size() > 0) {
	    	Helper.displayInfo("INFO: Adding ADDITION FILES");
	    	// Initial XML parent node element start
	    	AppProp.outputSolrFileAdditions.writeOutput(AppProp.outputFilenameAdditions, "<add>\n");
	    	String currentSolrXml = "";
	    	int addCount = 0;
	    	try {
	    		// Loop through each of the additions and add these to the output file
				while ((currentSolrXml = additionsQueue.poll(5, TimeUnit.SECONDS)) != null) {
					if (addCount == AppProp.totalCountPerFile) {
						AppProp.outputSolrFileAdditions.writeOutput(AppProp.outputFilenameAdditions, "</add>\n");
						addCount = 0;
						currentOutputFileCount++;
						AppProp.outputSolrFileAdditions = new OutputSolrFile();
						Helper.setCurrentOutputFilename(currentOutputFileCount, true);
						AppProp.outputSolrFileAdditions.writeOutput(AppProp.outputFilenameAdditions, "<add>\n");
					}
					Helper.displayInfo("INFO: Writing file to output file [" + AppProp.outputFilenameAdditions + "]");
					AppProp.outputSolrFileAdditions.writeOutput(AppProp.outputFilenameAdditions, currentSolrXml);
					addCount++;
				}
			} catch (Exception e) {
				Helper.recordError("Outputting Additions - " + e.getMessage());
			}
	    	// End the parent XML node element
	    	AppProp.outputSolrFileAdditions.writeOutput(AppProp.outputFilenameAdditions, "</add>");
	    	Helper.displayInfo("INFO: Finished Adding ADDITION FILES");
	    	Helper.postDataToSolr(AppProp.outputFilenameAdditions);
    	}
    	
    	System.out.println("Updated Files: " + FileMapping.updateCount);
    	System.out.println("Added Files: " + FileMapping.additionCount);
    	
    	
		AppProp.log.info("Ending job: " + AppProp.currentDate);
	}
}