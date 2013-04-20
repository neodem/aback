package com.neodem.aback.aws.simpledb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

public class Item {

	public static final String ITEMID_ATT = "itemId";

	private Map<String, String> attributes;
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getId());
		b.append(':');
		b.append(attributes.size());
		return b.toString();
	}

	public Item(String itemId) {
		attributes = new HashMap<String, String>();
		attributes.put(ITEMID_ATT, itemId);
	}

	public Item(List<Attribute> awsAttributes) {
		attributes = new HashMap<String, String>();
		for (Attribute a : awsAttributes) {
			attributes.put(a.getName(), a.getValue());
		}
	}

	public String getId() {
		return attributes.get(ITEMID_ATT);
	}

	public List<ReplaceableAttribute> getAwsAttributes() {
		List<ReplaceableAttribute> atts = new ArrayList<ReplaceableAttribute>(attributes.size());
		for (String key : attributes.keySet()) {
			atts.add(new ReplaceableAttribute(key, attributes.get(key), true));
		}
		return atts;
	}

	public void put(String name, String value) {
		attributes.put(name, value);
	}

	public String get(String name) {
		return attributes.get(name);
	}

	public Map<String, String> getAttributeMap() {
		return attributes;
	}
}
