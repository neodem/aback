package com.neodem.aback.aws.db;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
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
	public String getValue(String itemId, String key) {
		GetAttributesRequest req = new GetAttributesRequest(domain, itemId);
		req.setAttributeNames(Collections.singleton(key));
		GetAttributesResult result = sdb.getAttributes(req);
		List<Attribute> attributes = result.getAttributes();
		if (attributes == null || attributes.size() != 1) {
			return null;
		}
		return attributes.get(0).getValue();
	}

	@Override
	public Item getItemById(String itemId) {
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

	@Override
	public String setValue(String item, String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateValue(String item, String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setAwsCredentials(AWSCredentials awsCredentials) {
		this.awsCredentials = awsCredentials;
	}
}
