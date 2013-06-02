package com.neodem.aback.aws.simpledb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.Item;

public class AwsSimpleDbUtils {

	public static Map<String, String> makeAttributeMap(Item item) {
		if (item == null)
			return null;

		List<Attribute> attributes = item.getAttributes();

		Map<String, String> attMap = new HashMap<String, String>(attributes.size());

		for (Attribute a : attributes) {
			attMap.put(a.getName(), a.getValue());
		}

		return attMap;
	}

	public static Collection<Attribute> makeAttributeCollection(Map<String, String> metaMap) {
		Collection<Attribute> atts = new ArrayList<Attribute>(metaMap.size());

		for (String key : metaMap.keySet()) {
			String value = metaMap.get(key);
			Attribute a = new Attribute();
			a.setName(key);
			a.setValue(value);
			atts.add(a);
		}

		return atts;
	}

	/**
	 * Initialize a domain (create it if it doesn't exist)
	 */
	public static void initDomain(AmazonSimpleDB sdb, String domainName) {
		List<String> domainNames = sdb.listDomains().getDomainNames();
		if (!domainNames.contains(domainName)) {
			sdb.createDomain(new CreateDomainRequest(domainName));
		}
	}

	/**
	 * remove a domain
	 * 
	 * @param domainName
	 */
	public static void removeDomain(AmazonSimpleDB sdb, String domainName) {
		List<String> domainNames = sdb.listDomains().getDomainNames();
		if (domainNames.contains(domainName)) {
			sdb.deleteDomain(new DeleteDomainRequest(domainName));
		}
	}

}
