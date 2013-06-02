package com.neodem.aback.main;

import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.neodem.aback.aws.glacier.GlacierFileIO;
import com.neodem.aback.aws.simpledb.MetaItemId;
import com.neodem.aback.service.retreival.RetreivalItem;
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

	private static final Long TWOFIFTYSIXMEGS = 268435456l;

	private static Logger log = Logger.getLogger(RecoverRequest.class);

	private GlacierFileIO glacierFileIo;
	private TrackerService trackerService;
	private RetreivalManager retreivalManager;

	public void process(String vaultName) {
		Map<String, TrackerMetaItem> allRecords = trackerService.getAllRecords(vaultName);
		for (TrackerMetaItem meta : allRecords.values()) {
			String archiveId = meta.getArchiveId();

			Long filesize = meta.getFilesize();
			if (filesize != null && filesize > TWOFIFTYSIXMEGS) {
				// make many file reqests
			} else {
				Path originalPath = meta.getOriginalPath();
				MetaItemId id;
				try {
					id = new MetaItemId(archiveId, originalPath);
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException("could not make id : " + e.getMessage());
				}

				if (!retreivalManager.exists(vaultName, id)) {
					String jobId = glacierFileIo.initiateDownloadRequest(vaultName, archiveId, "retreive_file");
					RetreivalItem r = new RetreivalItem(jobId, originalPath, RetreivalManager.Status.Started);
					retreivalManager.addRetrievialItem(vaultName, id, r);
				}
			}
		}

		printResults(vaultName);
	}

	private void printResults(String vaultName) {
		Map<String, RetreivalItem> allRecords = retreivalManager.getAllRecords(vaultName);

		System.out.println("All Records");
		System.out.println("--------------");

		for (String id : allRecords.keySet()) {
			System.out.println(allRecords.get(id).toString());
		}
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
