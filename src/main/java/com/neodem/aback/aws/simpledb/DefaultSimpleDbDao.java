package com.neodem.aback.aws.simpledb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.neodem.aback.aws.simpledb.AwsSimpleDbService;
import com.neodem.aback.aws.simpledb.AwsItem;

public class DefaultSimpleDbDao implements SimpleDbDao {
	private AwsSimpleDbService dbService;

	/* (non-Javadoc)
	 * @see com.neodem.aback.aws.simpledb.SimpleDbDaos#metaItemExists(java.lang.String, com.neodem.aback.service.id.MetaItemId)
	 */
	@Override
	public boolean metaItemExists(String tablespace, MetaItemId id) {
		return dbService.itemExists(tablespace, id.getHash());
	}

	/* (non-Javadoc)
	 * @see com.neodem.aback.aws.simpledb.SimpleDbDaos#getMetaItem(java.lang.String, com.neodem.aback.service.id.MetaItemId)
	 */
	@Override
	public MetaItem getMetaItem(String tablespace, MetaItemId id) {
		AwsItem item = dbService.getItem(tablespace, id.getHash());
		if (item == null)
			return null;
		return makeMetaForItem(item);
	}

	/* (non-Javadoc)
	 * @see com.neodem.aback.aws.simpledb.SimpleDbDaos#saveMetaItem(java.lang.String, com.neodem.aback.service.id.MetaItemId, com.neodem.aback.aws.simpledb.MetaItem)
	 */
	@Override
	public void saveMetaItem(String tablespace, MetaItemId id, MetaItem meta) {
		AwsItem item = new AwsItem(id.getHash());

		Map<String, String> metaMap = meta.getMetaMap();
		for (String key : metaMap.keySet()) {
			item.addAttribute(key, metaMap.get(key));
		}

		dbService.saveItem(tablespace, item);
	}

	/* (non-Javadoc)
	 * @see com.neodem.aback.aws.simpledb.SimpleDbDaos#getAllItems(java.lang.String)
	 */
	@Override
	public Map<String, MetaItem> getAllItems(String tablespace) {
		Map<String, MetaItem> allRecords = new HashMap<String, MetaItem>();
		Collection<AwsItem> allItems = dbService.getAll(tablespace);
		for (AwsItem item : allItems) {
			MetaItem meta = makeMetaForItem(item);
			allRecords.put(item.getId(), meta);
		}

		return allRecords;
	}

	protected MetaItem makeMetaForItem(AwsItem item) {
		Map<String, String> attributeMap = item.getAttributeMap();
		
		MetaItem m = new MetaItem();
		m.setMetaMap(attributeMap);
		
		return m;
	}

	public void setDbService(AwsSimpleDbService dbService) {
		this.dbService = dbService;
	}
}
