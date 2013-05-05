package com.neodem.aback.main;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.neodem.aback.aws.glacier.GlacierFileIO;
import com.neodem.aback.service.id.IdService;
import com.neodem.aback.service.tracker.TrackerService;

public class Recover {

	private static Logger log = Logger.getLogger(Recover.class);

	private static ApplicationContext appContext;

	// the root dir for backup. All files from here down are backed up
	private Path restoreRoot;
	private String vaultName;

	private GlacierFileIO glacierFileIo;
	private TrackerService trackerService;
	private IdService idService;

	public void process(String[] args) {
		readArgs(args);
	}


	private void readArgs(String[] args) {
		restoreRoot = Paths.get(args[0]);
		vaultName = args[1];
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		appContext = new ClassPathXmlApplicationContext("Recover-context.xml");
		Recover recover = (Recover) appContext.getBean("recover-main");
		recover.process(args);
	}

	public void setGlacierFileIo(GlacierFileIO glacierFileIo) {
		this.glacierFileIo = glacierFileIo;
	}

	public void setTrackerService(TrackerService trackerService) {
		this.trackerService = trackerService;
	}

	public void setIdService(IdService idService) {
		this.idService = idService;
	}
}
