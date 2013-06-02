package com.neodem.aback.service.retreival;

import java.util.Map;

import com.neodem.aback.aws.simpledb.MetaItemId;

public interface RetreivalManager {

	public enum Status {
		Started
	}

	Map<String, RetreivalItem> getAllRecords(String vaultName);

	void addRetrievialItem(String vaultName, MetaItemId id, RetreivalItem r);

	boolean exists(String vaultName, MetaItemId id);

}
