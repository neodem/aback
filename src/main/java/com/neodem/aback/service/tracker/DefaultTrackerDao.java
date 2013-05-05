package com.neodem.aback.service.tracker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.neodem.aback.aws.simpledb.AwsSimpleDbService;
import com.neodem.aback.aws.simpledb.AwsItem;
import com.neodem.aback.service.id.MetaItemId;

public class DefaultTrackerDao implements TrackerDao {
	private AwsSimpleDbService dbService;

	@Override
	public boolean exists(String vaultName, MetaItemId fileId) {
		return dbService.itemExists(vaultName, fileId.getHash());
	}

	@Override
	public TrackerMetaItem getMetaItem(String vaultName, MetaItemId fileId) {
		AwsItem item = dbService.getItem(vaultName, fileId.getHash());
		if (item == null)
			return null;
		return makeMetaForItem(item);
	}

	@Override
	public void saveMetaItem(String vaultName, MetaItemId fileId, TrackerMetaItem meta) {
		AwsItem item = new AwsItem(fileId.getHash());

		Map<String, String> metaMap = meta.getMetaMap();
		for (String key : metaMap.keySet()) {
			item.addAttribute(key, metaMap.get(key));
		}

		dbService.saveItem(vaultName, item);
	}

	@Override
	public Map<String, TrackerMetaItem> getAllItems(String vaultName) {
		Map<String, TrackerMetaItem> allRecords = new HashMap<String, TrackerMetaItem>();
		Collection<AwsItem> allItems = dbService.getAll(vaultName);
		for (AwsItem item : allItems) {
			TrackerMetaItem meta = makeMetaForItem(item);
			allRecords.put(item.getId(), meta);
		}

		return allRecords;
	}

	private TrackerMetaItem makeMetaForItem(AwsItem item) {
		Map<String, String> attributeMap = item.getAttributeMap();
		return new TrackerMetaItem(attributeMap);
	}

	public void setDbService(AwsSimpleDbService dbService) {
		this.dbService = dbService;
	}
}
