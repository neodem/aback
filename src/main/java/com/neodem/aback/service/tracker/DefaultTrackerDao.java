package com.neodem.aback.service.tracker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.neodem.aback.aws.simpledb.AwsSimpleDbService;
import com.neodem.aback.aws.simpledb.AwsItem;
import com.neodem.aback.service.id.FileId;

public class DefaultTrackerDao implements TrackerDao {
	private AwsSimpleDbService dbService;

	@Override
	public boolean exists(FileId fileId) {
		return dbService.itemExists(fileId.getHash());
	}

	@Override
	public TrackerMetaItem getMeta(FileId fileId) {
		AwsItem item = dbService.getItem(fileId.getHash());
		if (item == null)
			return null;
		return makeMetaForItem(item);
	}

	@Override
	public void setMeta(FileId fileId, TrackerMetaItem meta) {
		AwsItem item = new AwsItem(fileId.getHash());
		
		Map<String, String> metaMap = meta.getMetaMap();
		for(String key : metaMap.keySet()) {
			item.put(key, metaMap.get(key));
		}
		
		dbService.saveItem(item);
	}
	
	@Override
	public Map<String, TrackerMetaItem> getAllRecords() {
		Map<String, TrackerMetaItem> allRecords = new HashMap<String, TrackerMetaItem>();
		Collection<AwsItem> allItems = dbService.getAll();
		for(AwsItem item : allItems) {
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
