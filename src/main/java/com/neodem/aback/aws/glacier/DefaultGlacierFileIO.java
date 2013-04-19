package com.neodem.aback.aws.glacier;

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
public class DefaultGlacierFileIO implements GlacierFileIO {

	private static Logger log = Logger.getLogger(DefaultGlacierFileIO.class);

	private AWSCredentials awsCredentials;
	private AmazonGlacierClient amazonGlacierClient;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.neodem.aback.aws.glacier.FileIO#writeFile(java.nio.file.Path)
	 */
	public String writeFile(Path path, String vaultName) {
		log.info("writeFile(" + path + "," + vaultName);

		UploadResult result;
		try {
			ArchiveTransferManager atm = new ArchiveTransferManager(amazonGlacierClient, awsCredentials);
			result = atm.upload(vaultName, "my archive " + (new Date()), path.toFile());
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		return result.getArchiveId();
	}

	public void setAwsCredentials(AWSCredentials awsCredentials) {
		this.awsCredentials = awsCredentials;
	}

	public void setAmazonGlacierClient(AmazonGlacierClient amazonGlacierClient) {
		this.amazonGlacierClient = amazonGlacierClient;
	}

}
