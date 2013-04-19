package com.neodem.aback.aws.db;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

/**
 * init the service to connect to a domain...
 * @author vfumo
 *
 */
public class DefaultDBService implements DB, InitializingBean {

	private AWSCredentials awsCredentials;
	private String domain;

	private AmazonSimpleDB sdb;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		sdb = new AmazonSimpleDBClient(awsCredentials);
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		sdb.setRegion(usWest2);

		List<String> domainNames = sdb.listDomains().getDomainNames();
		if (!domainNames.contains(domain)) {
			sdb.createDomain(new CreateDomainRequest(domain));
		}
	}
	
	@Override
	public boolean itemExists(String itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void saveItem(Item item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Item getItem(String itemId) {
		String selectExpression = "select * from `" + domain + "` where ID = '" + itemId + "'";
		SelectRequest selectRequest = new SelectRequest(selectExpression);
		SelectResult result = sdb.select(selectRequest);
		List<Item> items = result.getItems();
		if (items.isEmpty())
			return null;
		if (items.size() > 1)
			return null;
		return items.get(0);
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setAwsCredentials(AWSCredentials awsCredentials) {
		this.awsCredentials = awsCredentials;
	}


}
