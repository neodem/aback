package com.neodem.aback.aws.glacier;

import java.nio.file.Path;

public interface FileIO {
	public String writeFile(Path path, String vaultName);
}
