package com.neodem.aback.main;

import java.nio.file.Path;
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
 * for a given vaultName we will initialize requests for the data items in the
 * vault. We store them into the retreivalmanager. It is up to the Recoverer to
 * actually get the files (since it takes hours to get them ready for d/l)
 * 
 * @author vfumo
 * 
 */
public class RecoveryPoller {

	private static final Long TWOFIFTYSIXMEGS = 268435456l;

	private static Logger log = Logger.getLogger(RecoveryPoller.class);

	private GlacierFileIO glacierFileIo;
	private TrackerService trackerService;
	private RetreivalManager retreivalManager;

	public void process(String vaultName) {
	}


	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("RecoverRequest-context.xml");
		RecoveryPoller recover = (RecoveryPoller) appContext.getBean("recover-request");
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
