package com.neodem.aback.aws.simpledb;

import java.util.HashMap;
import java.util.Map;

public class MetaItem {
	
	protected Map<String, String> metaMap = new HashMap<>();

	public void setMetaMap(Map<String, String> metaMap) {
		this.metaMap = metaMap;
	}
	
	public Map<String, String> getMetaMap() {
		return metaMap;
	}
}
