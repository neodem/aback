package com.neodem.aback.aws.db;

import java.util.Collections;
import java.util.List;

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

public class DefaultDBService implements DB {

	private static final String DOMAINNAME = "com.neodem.aback.main";

	private AmazonSimpleDB sdb;

	public DefaultDBService(AWSCredentials creds) {
		sdb = new AmazonSimpleDBClient(creds);
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		sdb.setRegion(usWest2);

		List<String> domainNames = sdb.listDomains().getDomainNames();
		if (!domainNames.contains(DOMAINNAME)) {
			sdb.createDomain(new CreateDomainRequest(DOMAINNAME));
		}
	}

	@Override
	public String getValue(String itemId, String key) {
		GetAttributesRequest req = new GetAttributesRequest(DOMAINNAME, itemId);
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
		String selectExpression = "select * from `" + DOMAINNAME + "` where ID = '" + itemId + "'";
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

}
