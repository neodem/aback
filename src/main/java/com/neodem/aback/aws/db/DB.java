package com.neodem.aback.aws.db;

import com.amazonaws.services.simpledb.model.Item;

/**
 * note an Item is like a row in the database
 * the values are keyed
 * 
 * @author vfumo
 *
 */
public interface DB {
	
	/**
	 * return null if Item not found
	 * 
	 * @param itemId
	 * @return
	 */
	public Item getItem(String itemId);
	
	public boolean itemExists(String itemId);

	public void saveItem(Item item);
}
