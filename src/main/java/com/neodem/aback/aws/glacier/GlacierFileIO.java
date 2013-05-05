package com.neodem.aback.aws.glacier;

import java.nio.file.Path;

public interface GlacierFileIO {
	
	/**
	 * send the file to a Glacier vault and return an archiveId
	 * 
	 * @param file
	 * @param description
	 * @param vaultName 
	 * @return
	 */
	public String writeFile(Path file, String description, String vaultName) throws GlacierFileIOException;
}
