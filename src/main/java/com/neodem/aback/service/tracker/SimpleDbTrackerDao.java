package com.neodem.aback.service.tracker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.neodem.aback.aws.simpledb.dao.MetaItem;
import com.neodem.aback.aws.simpledb.dao.SimpleDbDao;

public class SimpleDbTrackerDao implements TrackerDao {

	private SimpleDbDao dao;

	@Override
	public boolean exists(String vaultName, String id) {
		return dao.metaItemExists(makeTs(vaultName), id);
	}

	@Override
	public TrackerMetaItem get(String vaultName, String id) {
		MetaItem metaItem = dao.getMetaItem(makeTs(vaultName), id);
		return makeTrackerFromMeta(metaItem);
	}

	@Override
	public void save(String vaultName, TrackerMetaItem meta) {
		dao.saveMetaItem(makeTs(vaultName), meta);
	}

	@Override
	public Map<String, TrackerMetaItem> getAll(String vaultName) {
		Map<String, MetaItem> allItems = dao.getAllMetaItems(makeTs(vaultName));
		Map<String, TrackerMetaItem> translatedItems = new HashMap<>();
		for (String key : allItems.keySet()) {
			translatedItems.put(key, makeTrackerFromMeta(allItems.get(key)));
		}

		return translatedItems;
	}
	
	@Override
	public void remove(String vaultName, String id) {
		dao.removeMetaItem(makeTs(vaultName), id);
	}

	private String makeTs(String vaultName) {
		return "TrackerDao." + vaultName;
	}

	private TrackerMetaItem makeTrackerFromMeta(MetaItem metaItem) {
		Map<String, String> attributeMap = metaItem.getMetaMap();
		TrackerMetaItem t = new TrackerMetaItem(metaItem.getId());
		t.setMetaMap(attributeMap);
		return t;
	}

	@Required
	public void setDao(SimpleDbDao dao) {
		this.dao = dao;
	}
}
