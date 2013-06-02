package com.neodem.aback.service.retreival;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.neodem.aback.aws.simpledb.MetaItem;
import com.neodem.aback.aws.simpledb.MetaItemId;
import com.neodem.aback.aws.simpledb.SimpleDbDao;

public class SimpleDbRetreivalManagerDao implements RetreivalManagerDao {
	private SimpleDbDao dao;

	@Required
	public void setDao(SimpleDbDao dao) {
		this.dao = dao;
	}

	@Override
	public void save(String vaultName, MetaItemId id, RetreivalItem r) {
		dao.saveMetaItem(makeTs(vaultName), id, r);
	}

	private String makeTs(String vaultName) {
		return "RetreivalManagerDao." + vaultName;
	}

	@Override
	public boolean itemExists(String vaultName, MetaItemId metaItemId) {
		return dao.metaItemExists(makeTs(vaultName), metaItemId);
	}

	@Override
	public RetreivalItem getItem(String vaultName, MetaItemId metaItemId) {
		MetaItem metaItem = dao.getMetaItem(makeTs(vaultName), metaItemId);
		return makeRetreivalItemFromMeta(metaItem);
	}

	@Override
	public Map<String, RetreivalItem> getAllItems(String vaultName) {
		Map<String, MetaItem> allItems = dao.getAllItems(makeTs(vaultName));
		Map<String, RetreivalItem> translatedItems = new HashMap<>();
		for (String key : allItems.keySet()) {
			translatedItems.put(key, makeRetreivalItemFromMeta(allItems.get(key)));
		}

		return translatedItems;
	}

	private RetreivalItem makeRetreivalItemFromMeta(MetaItem metaItem) {
		Map<String, String> attributeMap = metaItem.getMetaMap();
		RetreivalItem t = new RetreivalItem();
		t.setMetaMap(attributeMap);
		return t;
	}
}
