package com.neodem.aback.service.tracker;

import java.util.Map;

import com.neodem.aback.service.id.MetaItemId;

public interface TrackerDao {

	/**
	 * return if a fileId record exists.
	 * @param metaItemId
	 * 
	 * @return
	 */
	boolean exists(String vaultName, MetaItemId metaItemId);

	/**
	 * will return null if none found
	 * @param metaItemId
	 * @return
	 */
	TrackerMetaItem getMetaItem(String vaultName, MetaItemId metaItemId);

	/**
	 * 
	 * @param metaItemId
	 * @param meta
	 */
	void saveMetaItem(String vaultName, MetaItemId metaItemId, TrackerMetaItem meta);

	/**
	 * 
	 * @return
	 */
	Map<String, TrackerMetaItem> getAllItems(String vaultName);
}
