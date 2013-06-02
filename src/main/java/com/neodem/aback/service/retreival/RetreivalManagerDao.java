package com.neodem.aback.service.retreival;

import java.util.Map;

public interface RetreivalManagerDao {

	void save(String vaultName, RetreivalItem r);

	RetreivalItem getItem(String vaultName, String id);

	Map<String, RetreivalItem> getAllItems(String vaultName);

	boolean itemExists(String vaultName, String id);

	void remove(String vaultName, String id);
}
