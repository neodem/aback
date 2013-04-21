package com.neodem.aback.main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.neodem.aback.aws.glacier.GlacierFileIO;
import com.neodem.aback.aws.glacier.GlacierFileIOException;
import com.neodem.aback.service.id.FileId;
import com.neodem.aback.service.id.IdService;
import com.neodem.aback.service.scanner.ScannerService;
import com.neodem.aback.service.tracker.TrackerMetaItem;
import com.neodem.aback.service.tracker.TrackerService;

/**
 * @author vfumo
 * 
 */
public class ABack {

	private static Logger log = Logger.getLogger(ABack.class);

	private static ApplicationContext appContext;

	// the root dir for backup. All files from here down are backed up
	private Path sourceRoot;
	private String vaultName;

	private GlacierFileIO glacierFileIo;
	private ScannerService scannerService;
	private TrackerService trackerService;
	private IdService idService;

	public void process(String[] args) {
		readArgs(args);

		Map<Path, BasicFileAttributes> filesToBackup = scannerService.scan(sourceRoot);

		for (Path absolutePath : filesToBackup.keySet()) {
			Path relativePath = sourceRoot.relativize(absolutePath);
			
			FileId fileId = idService.makeIdForPath(relativePath);

			if (fileId == null) {
				log.warn("skipped since we couldn't make a fileId : " + absolutePath.toString());
			} else {
				if (trackerService.shouldBackup(fileId, filesToBackup.get(absolutePath))) {
					String archiveId;
					try {
						archiveId = glacierFileIo.writeFile(absolutePath, vaultName);
					} catch (GlacierFileIOException e) {
						String msg = "could not upload due to : " + e.getMessage();
						log.warn(msg);
						continue;
					} 
					log.info("backed up : " + absolutePath.toString() + " to " + vaultName + ":" + archiveId);
					trackerService.updateAll(fileId, archiveId, relativePath, new Date());
				} else {
					log.info(absolutePath.toString() + " does not need to backup");
				}
			}
		}
		
		printResults();
	}

	private void printResults() {
		Map<String, TrackerMetaItem> allRecords = trackerService.getAllRecords();

		System.out.println("All Records");
		System.out.println("--------------");
		
		for(String id : allRecords.keySet()) {
			System.out.println(allRecords.get(id).toString());
		}
	}

	private void readArgs(String[] args) {
		sourceRoot = Paths.get(args[0]);
		vaultName = args[1];
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		appContext = new ClassPathXmlApplicationContext("ABack-context.xml");
		ABack aback = (ABack) appContext.getBean("aback");
		aback.process(args);
	}

	public void setGlacierFileIo(GlacierFileIO glacierFileIo) {
		this.glacierFileIo = glacierFileIo;
	}

	public void setScannerService(ScannerService scannerService) {
		this.scannerService = scannerService;
	}

	public void setTrackerService(TrackerService trackerService) {
		this.trackerService = trackerService;
	}

	public void setIdService(IdService idService) {
		this.idService = idService;
	}

}
