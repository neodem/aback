package com.neodem.aback.service.tracker;

import java.util.Map;

import com.neodem.aback.aws.simpledb.MetaItemId;

public interface TrackerDao {

	/**
	 * return if a TrackerMetaItem record exists.
	 * @param metaItemId
	 * 
	 * @return
	 */
	boolean metaItemExists(String vaultName, MetaItemId metaItemId);

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
	Map<String, TrackerMetaItem> getAllTrackerItems(String vaultName);
}
