package com.neodem.aback.aws.glacier;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

/**
 * @author vfumo
 * 
 */
public class DefaultFileIO implements FileIO {

	private static Logger log = Logger.getLogger(DefaultFileIO.class);

	private AWSCredentials creds;
	private AmazonGlacierClient client;

	public DefaultFileIO(AWSCredentials creds, String endpoint) throws IOException {
		this.creds = creds;
		client = new AmazonGlacierClient(creds);
		client.setEndpoint(endpoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.neodem.aback.aws.glacier.FileIO#writeFile(java.nio.file.Path)
	 */
	public String writeFile(Path path, String vaultName) {
		log.info("writeFile(" + path + "," + vaultName);

		UploadResult result;
		try {
			ArchiveTransferManager atm = new ArchiveTransferManager(client, creds);
			result = atm.upload(vaultName, "my archive " + (new Date()), path.toFile());
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		return result.getArchiveId();
	}

}
