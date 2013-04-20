package com.neodem.aback.service.tracker;

import java.util.Map;

import com.neodem.aback.aws.simpledb.AwsSimpleDbService;
import com.neodem.aback.aws.simpledb.Item;
import com.neodem.aback.service.id.FileId;

public class DefaultTrackerDao implements TrackerDao {
	private AwsSimpleDbService dbService;

	@Override
	public boolean exists(FileId fileId) {
		return dbService.itemExists(fileId.getHash());
	}

	@Override
	public TrackerMetaItem getMeta(FileId fileId) {
		Item item = dbService.getItem(fileId.getHash());
		if (item == null)
			return null;
		return makeMetaForItem(item);
	}

	@Override
	public void setMeta(FileId fileId, TrackerMetaItem meta) {
		Item item = new Item(fileId.getHash());
		
		Map<String, String> metaMap = meta.getMetaMap();
		for(String key : metaMap.keySet()) {
			item.put(key, metaMap.get(key));
		}
		
		dbService.saveItem(item);
	}

	private TrackerMetaItem makeMetaForItem(Item item) {
		Map<String, String> attributeMap = item.getAttributeMap();
		return new TrackerMetaItem(attributeMap);
	}

	public void setDbService(AwsSimpleDbService dbService) {
		this.dbService = dbService;
	}

}
