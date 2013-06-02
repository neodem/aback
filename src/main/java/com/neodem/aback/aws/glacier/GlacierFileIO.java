package com.neodem.aback.aws.glacier;

import java.nio.file.Path;

public interface GlacierFileIO {

	/**
	 * send the file to a Glacier vault and return an archiveId
	 * 
	 * @param vaultName
	 * @param file
	 * @param description
	 * 
	 * @return
	 */
	public String writeFile(String vaultName, Path file, String description) throws GlacierFileIOException;
	
	/**
	 * 
	 * @param vaultName
	 * @param archiveId
	 * @param description
	 * @return
	 */
	public String initiateDownloadRequest(String vaultName, String archiveId, String description);

}
