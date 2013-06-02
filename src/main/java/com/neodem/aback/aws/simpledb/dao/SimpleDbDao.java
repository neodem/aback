package com.neodem.aback.aws.simpledb.dao;

import java.util.Map;

public interface SimpleDbDao {

	/**
	 * 
	 * @param tablespace
	 * @param item must have id set
	 */
	public abstract void saveMetaItem(String tablespace, MetaItem item);
	
	public abstract MetaItem getMetaItem(String tablespace, String id);

	/**
	 * 
	 * @param tablespace
	 * @return <id, item>
	 */
	public abstract Map<String, MetaItem> getAllMetaItems(String tablespace);

	public abstract void removeMetaItem(String tablespace, String id);

	public abstract boolean metaItemExists(String tablespace, String id);
}