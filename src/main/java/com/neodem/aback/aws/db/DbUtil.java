package com.neodem.aback.aws.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;

public class DbUtil {

	public static Map<String, String> makeAttMap(Item item) {
		if(item == null) return null;
		
		List<Attribute> attributes = item.getAttributes();

		Map<String,String> attMap = new HashMap<String,String>(attributes.size());
		
		for(Attribute a : attributes) {
			attMap.put(a.getName(), a.getValue());
		}
		
		return attMap;
	}

	public static Collection<Attribute> makeAttributeCollection(Map<String, String> metaMap) {
		Collection<Attribute> atts = new ArrayList<Attribute>(metaMap.size());

		for(String key : metaMap.keySet()) {
			String value = metaMap.get(key);
			Attribute a = new Attribute();
			a.setName(key);
			a.setValue(value);
			atts.add(a);
		}
		
		return atts;
	}

}
