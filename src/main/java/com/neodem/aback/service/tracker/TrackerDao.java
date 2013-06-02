package com.neodem.aback.service.tracker;

import java.util.Map;

public interface TrackerDao {

	/**
	 * return if a TrackerMetaItem record exists.
	 * 
	 * @param id
	 * @return
	 */
	boolean exists(String vaultName, String id);

	/**
	 * will return null if none found
	 * 
	 * @param id
	 * @return
	 */
	TrackerMetaItem get(String vaultName, String id);

	/**
	 * 
	 * @param meta
	 */
	void save(String vaultName, TrackerMetaItem meta);

	/**
	 * 
	 * @return
	 */
	Map<String, TrackerMetaItem> getAll(String vaultName);
	
	void remove(String vaultName, String id);
}
