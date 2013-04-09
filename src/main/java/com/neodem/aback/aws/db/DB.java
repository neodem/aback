package com.neodem.aback.aws.db;

import com.amazonaws.services.simpledb.model.Item;

public interface DB {
	
	public Item getItemById(String itemId);
	
	public String getValue(String itemId, String key);
	public String setValue(String itemId, String key, String value);
	
	/**
	 * will update a value or set it anew
	 * 
	 * @param item
	 * @param key
	 * @param value
	 * @return old value
	 */
	public String updateValue(String itemId, String key, String value);
}
