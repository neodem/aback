package com.neodem.aback.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.neodem.aback.aws.glacier.GlacierFileIO;
import com.neodem.aback.service.retreival.RetreivalItem;
import com.neodem.aback.service.retreival.RetreivalManager;
import com.neodem.aback.service.tracker.TrackerMetaItem;
import com.neodem.aback.service.tracker.TrackerService;

/**
 * @author vfumo
 * 
 */
public class Recover {

	private static Logger log = Logger.getLogger(Recover.class);

	private GlacierFileIO glacierFileIo;
	private RetreivalManager retreivalManager;

	public void process(Path targetRoot, String vaultName) {

		Map<String, RetreivalItem> allRecords = retreivalManager.getAllRecords(vaultName);
		for (RetreivalItem r : allRecords.values()) {
			String jobId = r.getJobId();
			String archiveId = r.getArchiveId();
			Path originalPath = r.getOriginalPath();
			Path restoreLocation = makeRestoreLocation(originalPath, targetRoot);
			
			if(r.isLargeFile()) {
				
			} else {
				glacierFileIo.getFile(vaultName, restoreLocation, jobId);
			}
			
			retreivalManager.removeRecord(vaultName, r);
		}
	}

	private Path makeRestoreLocation(Path originalPath, Path targetRoot) {
		Path restoreLocation = targetRoot.resolve(originalPath);
		Path parent = restoreLocation.getParent();
		try {
			Files.createDirectories(parent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return restoreLocation;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("RecoverRequest-context.xml");
		Recover recover = (Recover) appContext.getBean("recover");
		Path targetRoot = Paths.get(args[0]);
		String vaultName = args[1];
		recover.process(targetRoot, vaultName);
	}

	public void setGlacierFileIo(GlacierFileIO glacierFileIo) {
		this.glacierFileIo = glacierFileIo;
	}

	public void setRetreivalManager(RetreivalManager retreivalManager) {
		this.retreivalManager = retreivalManager;
	}
}
