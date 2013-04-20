package com.neodem.aback.aws.simpledb;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

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

	private AWSCredentials awsCredentials;
	private String domain;

	private AmazonSimpleDB sdb;

	@Override
	public void afterPropertiesSet() throws Exception {
		sdb = new AmazonSimpleDBClient(awsCredentials);
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		sdb.setRegion(usWest2);
	}

	@Override
	public boolean itemExists(String itemId) {
		String query = "select count(*) from " + domain + " where " + Item.ITEMID_ATT + " = '" + itemId + "'";
		SelectResult select = sdb.select(new SelectRequest(query));
		int count = new Integer(select.getItems().get(0).getAttributes().get(0).getValue());
		return count == 1;
	}

	@Override
	public void saveItem(Item item) {
		sdb.putAttributes(new PutAttributesRequest(domain, item.getId(), item.getAwsAttributes()));
	}

	@Override
	public Item getItem(String itemId) {
		GetAttributesResult result = sdb.getAttributes(new GetAttributesRequest(domain, itemId));
		List<Attribute> attributes = result.getAttributes();
		return new Item(attributes);
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setAwsCredentials(AWSCredentials awsCredentials) {
		this.awsCredentials = awsCredentials;
	}

	/**
	 * 
	 */
	public void initDomain(String domainName) {
		List<String> domainNames = sdb.listDomains().getDomainNames();
		if (!domainNames.contains(domainName)) {
			sdb.createDomain(new CreateDomainRequest(domainName));
		}
		domain = domainName;
	}

	public void removeDomain(String domainName) {
		List<String> domainNames = sdb.listDomains().getDomainNames();
		if (domainNames.contains(domainName)) {
			sdb.deleteDomain(new DeleteDomainRequest(domainName));
		}
	}

	@Override
	public void removeItem(Item item) {
		sdb.deleteAttributes(new DeleteAttributesRequest(domain, item.getId()));
	}
}
