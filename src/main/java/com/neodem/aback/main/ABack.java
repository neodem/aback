package com.neodem.aback.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.neodem.aback.aws.glacier.DefaultFileIO;
import com.neodem.aback.aws.glacier.FileIO;

/**
 * @author vfumo
 *
 */
public class ABack {

	private static Logger log = Logger.getLogger(ABack.class);
	
	public ABack(String[] args) throws IOException {
		Path root = Paths.get(args[0]);
		
		Path props = root.resolve("AwsCredentials.properties");
		AWSCredentials creds = new PropertiesCredentials(Files.newInputStream(props));
		FileIO io = new DefaultFileIO(creds, args[2]);
		Path path = root.resolve("testFile");
		String archiveID = io.writeFile(path, args[1]);
		log.info("archiveId = '" + archiveID + "'");
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new ABack(args);
	}

}
