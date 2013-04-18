package com.neodem.aback.main;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.neodem.aback.aws.glacier.DefaultGlacierFileIO;
import com.neodem.aback.aws.glacier.GlacierFileIO;
import com.neodem.aback.service.id.AwsBackedIdService;
import com.neodem.aback.service.id.FileId;
import com.neodem.aback.service.id.IdService;
import com.neodem.aback.service.scanner.InMemoryScanner;
import com.neodem.aback.service.scanner.ScannerService;
import com.neodem.aback.service.tracker.DefaultTrackerService;
import com.neodem.aback.service.tracker.TrackerService;
import com.neodem.aback.spring.ApplicationContextUtils;

/**
 * @author vfumo
 * 
 */
public class ABack {

	private static Logger log = Logger.getLogger(ABack.class);

	// the root dir for backup. All files from here down are backed up
	private Path sourceRoot;

	private String endpointUrl;
	private String vaultName;

	public ABack(String[] args) throws IOException {
		ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();

		readArgs(args);
		AWSCredentials creds = initAWS();

		GlacierFileIO io = new DefaultGlacierFileIO(creds, endpointUrl);
		ScannerService s = new InMemoryScanner();
		TrackerService t = new DefaultTrackerService(creds);
		IdService id = new AwsBackedIdService(creds);

		Map<Path, BasicFileAttributes> filesToBackup = s.scan(sourceRoot);

		for (Path file : filesToBackup.keySet()) {
			FileId fileId = id.makeIdForPath(sourceRoot, file);
			if (t.shouldBackup(fileId, filesToBackup.get(file))) {
				String archiveId = io.writeFile(file, vaultName);
				t.updateArchiveId(fileId, archiveId);
				log.info("backed up : " + file.toString());
			}
		}
	}

	private AWSCredentials initAWS() {
		AWSCredentialsProvider credsprovider = new ClasspathPropertiesFileCredentialsProvider();
		AWSCredentials creds = credsprovider.getCredentials();
		return creds;
	}

	private void readArgs(String[] args) {
		sourceRoot = Paths.get(args[0]);
		endpointUrl = args[2];
		vaultName = args[1];
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new ABack(args);
	}

}
