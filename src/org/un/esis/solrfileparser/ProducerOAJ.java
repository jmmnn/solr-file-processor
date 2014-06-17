package org.un.esis.solrfileparser;


import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class ProducerOAJ implements IProducer {
	
	static int processedCount = 0;
	static int skippedCount = 0;
	BlockingQueue<String> additionsQueue;
	BlockingQueue<String> deletionsQueue;
	String[] judgesToExclude;
	
	public ProducerOAJ(BlockingQueue<String> additionsQueue, BlockingQueue<String> deletionsQueue, String[] judgesToExclude)
	{
		this.additionsQueue = additionsQueue;
		this.deletionsQueue = deletionsQueue;
		this.judgesToExclude = judgesToExclude;
	}

	public void run() {
		try {
        	Helper.displayInfo("INFO: Iterating files on FTP Server and additing to queues");
        	
        	if (Helper.getFTPConnection()) {
        	
	        	Helper.iterateFTPFiles("", "", 0, new Callable<Boolean>() {
	        		   public Boolean call() {
	        		        return applyBusinessLogic();
	        		   }
	        	});
	        	
	            Helper.displayInfo("INFO: Finished adding files to queues");
	            ConsumerOAJ consumer = new ConsumerOAJ(additionsQueue, deletionsQueue);
	            new Thread(consumer).start();
        	}

		} catch (Exception e) {
			Helper.recordError("ERROR: iterating files: " + e.getMessage());
		}	
	}
	
	@SuppressWarnings("unchecked")
	public boolean applyBusinessLogic()
	{
		String currentFilename = AppProp.currentlyProcessingFilename;
		
		if (currentFilename.contains("/unat/") || currentFilename.contains("/undt/"))
		{
			if ((currentFilename.contains("/judgments/") || currentFilename.contains("/orders/")) && currentFilename.endsWith(".pdf"))
			{			
				Helper.displayInfo("INFO: Identifying Type and Subtype");
				String type = (currentFilename.contains("unat") ? "Appeals Tribunal" : "Dispute Tribunal");
				String subtype = (currentFilename.contains("judgments") ? "Judgment" : "Order");
				Helper.displayInfo("INFO: Processing OAJ File" + currentFilename);
				Map<String, Object> allData = Helper.extractDataViaFTP(currentFilename);
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
				}
				Helper.displayInfo("INFO: Determining Language");
				// Determine the language - we only want to deal with English and French otherwise dont proceed
				String lc = Helper.getLanguageCode(fullText);
				if (lc.equals("fr") || lc.equals("en")) {
					MetaFileOAJ metaFile = new MetaFileOAJ();
					metaFile.setFilename(currentFilename);
					metaFile.setFullText(fullText);
					metaFile.setJudgesToExclude(judgesToExclude);
					metaFile.setMetaData(metaData);
					metaFile.setLanguageCode(lc);
					metaFile.setType(type);
					metaFile.setSubtype(subtype);

					Helper.displayInfo("INFO: Mapping File");
					// Map the file - and return the associates SolrXML
	                FileMapping.mapFileOAJ(metaFile, additionsQueue, deletionsQueue);
	                Helper.displayInfo("INFO: Finished Mapping File");
	                Helper.displayInfo("INFO: Adding Addition File to Queue");
	                processedCount += 1;
				}
				else {
					skippedCount += 1;
				}

			}
		}
		return true;
    }
}
