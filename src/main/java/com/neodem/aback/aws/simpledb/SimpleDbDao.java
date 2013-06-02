package com.neodem.aback.aws.simpledb;

import java.util.Map;


public interface SimpleDbDao {

	public abstract boolean metaItemExists(String tablespace, MetaItemId id);

	public abstract MetaItem getMetaItem(String tablespace, MetaItemId id);

	public abstract void saveMetaItem(String tablespace, MetaItemId id, MetaItem meta);

	public abstract Map<String, MetaItem> getAllItems(String tablespace);

}