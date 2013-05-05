package com.neodem.aback.service.retreival;

import java.nio.file.Path;

public interface RetreivalManager {

	public enum Status {
		Started
	}

	void addRetrievialItem(String jobId, Path originalPath, Status status);

}
