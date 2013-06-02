package com.neodem.aback.aws.simpledb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

/**
 * init the service to connect to a domain...
 * 
 * @author vfumo
 * 
 */
public class AwsSimpleDbServiceImpl implements AwsSimpleDbService, InitializingBean {
	
	protected static final String TABLESPACE_KEY = "tablespace";

	private static Logger log = Logger.getLogger(AwsSimpleDbServiceImpl.class);

	private AWSCredentials awsCredentials;
	private String domain;

	private AmazonSimpleDB sdb;

	@Override
	public void afterPropertiesSet() throws Exception {
		sdb = new AmazonSimpleDBClient(awsCredentials);
		sdb.setRegion(Region.getRegion(Regions.US_WEST_2));
		
		initDomain(domain);
	}

	@Override
	public AwsItem getItem(String tablespace, String itemId) {
		String itemName = makeItemName(tablespace, itemId);
		GetAttributesResult result = sdb.getAttributes(new GetAttributesRequest(domain, itemName));
		List<Attribute> attributes = result.getAttributes();
		return new AwsItem(attributes);
	}

	@Override
	public void saveItem(String tablespace, AwsItem item) {
		if(!item.containsAttribute(TABLESPACE_KEY)) {
			item.addAttribute(TABLESPACE_KEY, tablespace);
		}
		
		String itemName = makeItemName(tablespace, item.getId());
		sdb.putAttributes(new PutAttributesRequest(domain, itemName, item.getAwsAttributes()));
	}

	@Override
	public void removeItem(String tablespace, AwsItem item) {
		String itemName = makeItemName(tablespace, item.getId());
		sdb.deleteAttributes(new DeleteAttributesRequest(domain, itemName));
	}

	@Override
	public Collection<AwsItem> getAll(String tablespace) {
		String query = "select * from `" + domain + "` where " + TABLESPACE_KEY + " = '" + tablespace + "'";
		List<Item> items = getManyItems(query);
		Collection<AwsItem> results = new ArrayList<AwsItem>();

		for (com.amazonaws.services.simpledb.model.Item item : items) {
			results.add(new AwsItem(item.getAttributes()));
		}

		return results;
	}

	@Override
	public boolean itemExists(String tablespace, String itemId) {
		String query = "select count(*) from `" + domain + "` where `" + AwsItem.ITEMID_ATT + "` = '" + itemId + "' and `" + TABLESPACE_KEY
				+ "` = '" + tablespace + "'";
		SelectResult select = sdb.select(new SelectRequest(query));
		int count = new Integer(select.getItems().get(0).getAttributes().get(0).getValue());
		return count == 1;
	}

	private String makeItemName(String tablespace, String itemId) {
		return tablespace + itemId;
	}

	/**
	 * helper to read a lot of items
	 * 
	 * @param query
	 * @return
	 */
	protected List<Item> getManyItems(String query) {
		log.debug("Executing query: " + query);
		List<Item> items = new ArrayList<Item>();

		String nextToken = null;
		do {
			SelectRequest selectRequest = new SelectRequest(query);
			selectRequest.setConsistentRead(false);

			if (nextToken != null) {
				selectRequest.setNextToken(nextToken);
			}

			SelectResult result = sdb.select(selectRequest);
			items.addAll(result.getItems());
			nextToken = result.getNextToken();

		} while (nextToken != null);

		log.debug("Found matching items: " + items.size());
		return items;
	}

	/**
	 * 
	 */
	public void initDomain(String domainName) {
		List<String> domainNames = sdb.listDomains().getDomainNames();
		if (!domainNames.contains(domainName)) {
			sdb.createDomain(new CreateDomainRequest(domainName));
		}
	}

	public void removeDomain(String domainName) {
		List<String> domainNames = sdb.listDomains().getDomainNames();
		if (domainNames.contains(domainName)) {
			sdb.deleteDomain(new DeleteDomainRequest(domainName));
		}
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Required
	public void setAwsCredentials(AWSCredentials awsCredentials) {
		this.awsCredentials = awsCredentials;
	}

}
