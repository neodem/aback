package com.neodem.aback.service.retreival;

import java.util.Map;

import com.neodem.aback.aws.simpledb.MetaItemId;

public interface RetreivalManagerDao {

	void save(String vaultName, MetaItemId id, RetreivalItem r);
	
	boolean itemExists(String vaultName, MetaItemId metaItemId);

	RetreivalItem getItem(String vaultName, MetaItemId metaItemId);

	Map<String, RetreivalItem> getAllItems(String vaultName);

}
