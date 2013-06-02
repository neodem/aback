package com.neodem.aback.service.retreival;

import java.util.Map;

public interface RetreivalManager {

	public enum Status {
		Started, Ready
	}

	Map<String, RetreivalItem> getAllRetreivalItems(String vaultName);

	void addRetreivalItem(String vaultName, RetreivalItem r);

	boolean retreivalItemExists(String vaultName, String id);

	void removeRetreivalItem(String vaultName, RetreivalItem r);

}
