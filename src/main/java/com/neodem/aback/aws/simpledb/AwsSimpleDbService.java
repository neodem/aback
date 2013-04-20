package com.neodem.aback.aws.simpledb;


/**
 * note an Item is like a row in the database the values are keyed
 * 
 * @author vfumo
 * 
 */
public interface AwsSimpleDbService {

	/**
	 * return null if Item not found
	 * 
	 * @param itemId
	 * @return
	 */
	public Item getItem(String itemId);

	public boolean itemExists(String itemId);

	public void saveItem(Item item);

	public void removeItem(Item item);

	public void initDomain(String domainName);

	public void removeDomain(String domainName);
}
