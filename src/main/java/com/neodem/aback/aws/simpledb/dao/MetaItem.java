package com.neodem.aback.aws.simpledb.dao;

import java.util.HashMap;
import java.util.Map;

public class MetaItem {
	
	private String id;
	
	protected Map<String, String> metaMap = new HashMap<>();
	
	public MetaItem(String id) {
		this.id = id;
	}

	public void setMetaMap(Map<String, String> metaMap) {
		this.metaMap = metaMap;
	}
	
	public Map<String, String> getMetaMap() {
		return metaMap;
	}

	public String getId() {
		return id;
	}

	public Map<String, String> addMeta(String key, String value) {
		metaMap.put(key, value);
		return metaMap;
	}

	@Override
	public String toString() {
		return "MetaItem [id=" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 443;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaItem other = (MetaItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
