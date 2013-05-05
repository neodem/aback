package com.neodem.aback.aws.simpledb;

import java.util.Collection;

/**
 * note an Item is like a row in the database the values are keyed
 * 
 * @author vfumo
 * 
 */
public interface AwsSimpleDbService {
	/**
	 * 
	 * @param vaultName
	 * @param itemId
	 * @return
	 */
	public AwsItem getItem(String vaultName, String itemId);

	/**
	 * 
	 * @param item
	 */
	public void saveItem(String vaultName, AwsItem item);

	/**
	 * 
	 * @param item
	 */
	public void removeItem(String vaultName, AwsItem item);

	/**
	 * 
	 * @param vaultName
	 * @return
	 */
	public Collection<AwsItem> getAll(String vaultName);

	/**
	 * 
	 * @param vaultName
	 * @param itemId
	 * @return
	 */
	public boolean itemExists(String vaultName, String itemId);

}
