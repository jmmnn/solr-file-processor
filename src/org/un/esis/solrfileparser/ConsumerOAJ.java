package org.un.esis.solrfileparser;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

//Author: Kevin Bradley
//Date: 17-June-2014
//Description: This is the consumer class for OAJ called after the producer populates the queues
//Version: 1.0
//Code Reviewer:
public class ConsumerOAJ implements IConsumer {
	
	// Obtain a local copy of the queues passed by param
	BlockingQueue<String> additionsQueue;
	BlockingQueue<String> deletionsQueue;
	
	public ConsumerOAJ(BlockingQueue<String> additionsQueue, BlockingQueue<String> deletionsQueue)
	{
		this.additionsQueue = additionsQueue;
		this.deletionsQueue = deletionsQueue;
	}

	public void run() {
		// Ensure there are deletions
    	if (deletionsQueue.size() > 0) {
    		Helper.displayInfo("INFO: Adding DELETION FILES");
    		// Change the output file to the deletions one, passing the filename
    		AppProp.outputSolrFileDeletions.writeOutput(AppProp.outputFilenameDeletions, "<delete>\n");
	    	String currentSolrFileId = "";
	    	try {
	    		// Loop through each of the deletions and add these to the output file - wait 5 seconds
				while ((currentSolrFileId = deletionsQueue.poll(5, TimeUnit.SECONDS)) != null) {
					// Loop and output the id for any items identified as to be deleted
					AppProp.outputSolrFileDeletions.writeOutput(AppProp.outputFilenameDeletions, "<id>" + currentSolrFileId + "</id>\n");
				}
			} catch (Exception e) {
				Helper.recordError("Outputting Deletion - " + e.getMessage());
			}
	    	// Close the parent XML node
	    	AppProp.outputSolrFileDeletions.writeOutput(AppProp.outputFilenameDeletions, "</delete>\n");
	    	Helper.displayInfo("INFO: Finished Adding DELETION FILES");
	    	// Post the deletions output file to Solr
	    	Helper.postDataToSolr(AppProp.outputFilenameDeletions);
    	}
    	
    	// Ensure there are additions
    	if (additionsQueue.size() > 0) {
	    	Helper.displayInfo("INFO: Adding ADDITION FILES");
	    	// Initial XML parent node element start
	    	AppProp.outputSolrFileAdditions.writeOutput(AppProp.outputFilenameAdditions, "<add>\n");
	    	String currentSolrXml = "";
	    	try {
	    		// Loop through each of the additions and add these to the output file
				while ((currentSolrXml = additionsQueue.poll(5, TimeUnit.SECONDS)) != null) {
					// Loop and output the solr xml constructured by the producer
					AppProp.outputSolrFileAdditions.writeOutput(AppProp.outputFilenameAdditions, currentSolrXml);
				}
			} catch (Exception e) {
				Helper.recordError("Outputting Additions - " + e.getMessage());
			}
	    	// End the parent XML node element
	    	AppProp.outputSolrFileAdditions.writeOutput(AppProp.outputFilenameAdditions, "</add>");
	    	Helper.displayInfo("INFO: Finished Adding ADDITION FILES");
	    	// Post the additions output file to Solr
	    	Helper.postDataToSolr(AppProp.outputFilenameAdditions);
    	}
    	
    	System.out.println("Updated Files: " + FileMapping.updateCount);
    	System.out.println("Added Files: " + FileMapping.additionCount);
    	
		AppProp.log.info("Ending job: " + AppProp.currentDate);
	}

}
