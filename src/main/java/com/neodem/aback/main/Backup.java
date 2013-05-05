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
import com.neodem.aback.service.id.MetaItemId;
import com.neodem.aback.service.id.IdService;
import com.neodem.aback.service.scanner.ScannerService;
import com.neodem.aback.service.tracker.TrackerMetaItem;
import com.neodem.aback.service.tracker.TrackerService;

/**
 * params are sourceRoot : which is the root of where we want to backup from and
 * the vaultName to backup to.
 * 
 * There should be a different vaultName per backup 'set'
 * 
 * @author vfumo
 * 
 */
public class Backup {

	private static Logger log = Logger.getLogger(Backup.class);

	private GlacierFileIO glacierFileIo;
	private ScannerService scannerService;
	private TrackerService trackerService;
	private IdService idService;

	/**
	 * 
	 * @param sourceRoot
	 *            the root dir from where to backup files
	 * @param vaultName
	 *            the vault to put the files into (this is essentially the
	 *            identifier for the backup job)
	 */
	public void process(Path sourceRoot, String vaultName) {
		Map<Path, BasicFileAttributes> filesToBackup = scannerService.scan(sourceRoot);

		for (Path absolutePath : filesToBackup.keySet()) {
			Path relativePath = sourceRoot.relativize(absolutePath);

			MetaItemId fileId = idService.makeId(relativePath);

			if (fileId == null) {
				log.warn("skipped since we couldn't make a fileId : " + absolutePath.toString());
			} else {
				if (trackerService.shouldBackup(vaultName, fileId, filesToBackup.get(absolutePath))) {
					String archiveId;
					try {
						archiveId = glacierFileIo.writeFile(vaultName, absolutePath, fileId.getHash());
					} catch (GlacierFileIOException e) {
						String msg = "could not upload due to : " + e.getMessage();
						log.warn(msg);
						continue;
					}
					log.info("backed up : " + absolutePath.toString() + " to " + vaultName + ":" + archiveId);
					trackerService.register(vaultName, fileId, relativePath, archiveId, new Date());
				} else {
					log.info(absolutePath.toString() + " does not need to backup");
				}
			}
		}

		printResults(vaultName);
	}

	private void printResults(String vaultName) {
		Map<String, TrackerMetaItem> allRecords = trackerService.getAllRecords(vaultName);

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
		ApplicationContext appContext = new ClassPathXmlApplicationContext("Backup-context.xml");
		Backup aback = (Backup) appContext.getBean("backup-main");
		Path sourceRoot = Paths.get(args[0]);
		String vaultName = args[1];
		aback.process(sourceRoot, vaultName);
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
