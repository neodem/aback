package com.neodem.aback.aws.simpledb;

import java.util.Collection;

/**
 * tablespace = table 
 * AwsItem = row in table
 * 
 * @author vfumo
 * 
 */
public interface AwsSimpleDbService {

	/**
	 * 
	 * @param tablespace
	 * @param itemId
	 * @return
	 */
	public AwsItem getItem(String tablespace, String itemId);

	/**
	 * @param tablespace
	 * @param itemId
	 * @param item
	 */
	public void saveItem(String tablespace, AwsItem item);

	/**
	 * 
	 * @param item
	 */
	public void removeItem(String tablespace, String itemId);

	/**
	 * 
	 * @param tablespace
	 * @return
	 */
	public Collection<AwsItem> getAllItems(String tablespace);

	/**
	 * 
	 * @param tablespace
	 * @param itemId
	 * @return
	 */
	public boolean itemExists(String tablespace, String itemId);

}
