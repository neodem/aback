package com.neodem.aback.service.tracker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.neodem.aback.aws.simpledb.MetaItem;
import com.neodem.aback.aws.simpledb.MetaItemId;
import com.neodem.aback.aws.simpledb.SimpleDbDao;

public class SimpleDbTrackerDao implements TrackerDao {

	private SimpleDbDao dao;

	@Override
	public TrackerMetaItem getMetaItem(String vaultName, MetaItemId fileId) {
		MetaItem metaItem = dao.getMetaItem(makeTs(vaultName), fileId);
		return makeTrackerFromMeta(metaItem);
	}

	@Override
	public Map<String, TrackerMetaItem> getAllTrackerItems(String vaultName) {
		Map<String, MetaItem> allItems = dao.getAllItems(makeTs(vaultName));
		Map<String, TrackerMetaItem> translatedItems = new HashMap<>();
		for (String key : allItems.keySet()) {
			translatedItems.put(key, makeTrackerFromMeta(allItems.get(key)));
		}

		return translatedItems;
	}

	@Override
	public void saveMetaItem(String vaultName, MetaItemId metaItemId, TrackerMetaItem meta) {
		dao.saveMetaItem(makeTs(vaultName), metaItemId, meta);
	}
	
	@Override
	public boolean metaItemExists(String vaultName, MetaItemId metaItemId) {
		return dao.metaItemExists(makeTs(vaultName), metaItemId);
	}

	private String makeTs(String vaultName) {
		return "TrackerDao." + vaultName;
	}

	private TrackerMetaItem makeTrackerFromMeta(MetaItem metaItem) {
		Map<String, String> attributeMap = metaItem.getMetaMap();
		TrackerMetaItem t = new TrackerMetaItem();
		t.setMetaMap(attributeMap);
		return t;
	}

	@Required
	public void setDao(SimpleDbDao dao) {
		this.dao = dao;
	}

}
