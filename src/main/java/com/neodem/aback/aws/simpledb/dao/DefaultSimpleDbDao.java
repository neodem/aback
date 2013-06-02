package com.neodem.aback.aws.simpledb.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.neodem.aback.aws.simpledb.AwsSimpleDbService;
import com.neodem.aback.aws.simpledb.AwsItem;

public class DefaultSimpleDbDao implements SimpleDbDao {
	private AwsSimpleDbService dbService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.neodem.aback.aws.simpledb.SimpleDbDaos#metaItemExists(java.lang.String
	 * , com.neodem.aback.service.id.MetaItemId)
	 */
	@Override
	public boolean metaItemExists(String tablespace, String id) {
		return dbService.itemExists(tablespace, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.neodem.aback.aws.simpledb.SimpleDbDaos#getMetaItem(java.lang.String,
	 * com.neodem.aback.service.id.MetaItemId)
	 */
	@Override
	public MetaItem getMetaItem(String tablespace, String id) {
		AwsItem item = dbService.getItem(tablespace, id);
		if (item == null)
			return null;
		return makeMetaForItem(item, id);
	}

	@Override
	public void removeMetaItem(String tablespace, String id) {
		dbService.removeItem(tablespace, id);
	}

	@Override
	public void saveMetaItem(String tablespace, MetaItem item) {
		String id = item.getId();
		if(StringUtils.isBlank(id)) {
			throw new IllegalArgumentException("MetaItem must have non blank id");
		}
		
		AwsItem awsItem = new AwsItem(item.getId());

		Map<String, String> metaMap = item.getMetaMap();
		for (String key : metaMap.keySet()) {
			awsItem.addAttribute(key, metaMap.get(key));
		}

		dbService.saveItem(tablespace, awsItem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.neodem.aback.aws.simpledb.SimpleDbDaos#getAllItems(java.lang.String)
	 */
	@Override
	public Map<String, MetaItem> getAllMetaItems(String tablespace) {
		Map<String, MetaItem> allRecords = new HashMap<String, MetaItem>();
		Collection<AwsItem> allItems = dbService.getAllItems(tablespace);
		for (AwsItem item : allItems) {
			String id = item.getId();
			MetaItem meta = makeMetaForItem(item, id);
			allRecords.put(id, meta);
		}

		return allRecords;
	}

	protected MetaItem makeMetaForItem(AwsItem item, String id) {
		Map<String, String> attributeMap = item.getAttributeMap();

		MetaItem m = new MetaItem(id);
		m.setMetaMap(attributeMap);
		return m;
	}

	public void setDbService(AwsSimpleDbService dbService) {
		this.dbService = dbService;
	}

}
