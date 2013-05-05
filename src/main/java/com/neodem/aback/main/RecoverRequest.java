package com.neodem.aback.main;

import java.nio.file.Path;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.neodem.aback.aws.glacier.GlacierFileIO;
import com.neodem.aback.service.retreival.RetreivalManager;
import com.neodem.aback.service.tracker.TrackerMetaItem;
import com.neodem.aback.service.tracker.TrackerService;

/**
 * for a given vaultName we will initialize requests for the data items in the
 * vault. We store them into the retreivalmanager. It is up to the Recoverer to
 * actually get the files (since it takes hours to get them ready for d/l)
 * 
 * @author vfumo
 * 
 */
public class RecoverRequest {

	private static Logger log = Logger.getLogger(RecoverRequest.class);

	private GlacierFileIO glacierFileIo;
	private TrackerService trackerService;
	private RetreivalManager retreivalManager;

	public void process(String vaultName) {
		Map<String, TrackerMetaItem> allRecords = trackerService.getAllRecords(vaultName);
		for (TrackerMetaItem meta : allRecords.values()) {
			String archiveId = meta.getArchiveId();

			String jobId = inititateJobRequest(vaultName, archiveId);
			Path originalPath = meta.getOriginalPath();
			
			retreivalManager.addRetrievialItem(jobId, originalPath, RetreivalManager.Status.Started);
		}
	}

	private String inititateJobRequest(String vaultName, String archiveId) {
		return null;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("RecoverRequest-context.xml");
		RecoverRequest recover = (RecoverRequest) appContext.getBean("recover-request");
		String vaultName = args[0];
		recover.process(vaultName);
	}

	public void setGlacierFileIo(GlacierFileIO glacierFileIo) {
		this.glacierFileIo = glacierFileIo;
	}

	public void setTrackerService(TrackerService trackerService) {
		this.trackerService = trackerService;
	}

	public void setRetreivalManager(RetreivalManager retreivalManager) {
		this.retreivalManager = retreivalManager;
	}
}
