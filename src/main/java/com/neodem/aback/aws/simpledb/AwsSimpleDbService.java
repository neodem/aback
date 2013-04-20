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
	 * return null if Item not found
	 * 
	 * @param itemId
	 * @return
	 */
	public AwsItem getItem(String itemId);

	public boolean itemExists(String itemId);

	public void saveItem(AwsItem item);

	public void removeItem(AwsItem item);

	public void initDomain(String domainName);

	public void removeDomain(String domainName);

	public Collection<AwsItem> getAll();
}
