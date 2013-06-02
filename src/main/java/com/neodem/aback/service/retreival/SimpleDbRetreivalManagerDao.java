package com.neodem.aback.service.retreival;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.neodem.aback.aws.simpledb.dao.MetaItem;
import com.neodem.aback.aws.simpledb.dao.SimpleDbDao;

public class SimpleDbRetreivalManagerDao implements RetreivalManagerDao {
	private SimpleDbDao dao;

	@Required
	public void setDao(SimpleDbDao dao) {
		this.dao = dao;
	}
	
	@Override
	public void save(String vaultName, RetreivalItem r) {
		dao.saveMetaItem(makeTs(vaultName), r);
		
	}

	@Override
	public RetreivalItem getItem(String vaultName, String id) {
		MetaItem metaItem = dao.getMetaItem(makeTs(vaultName), id);
		return makeRetreivalItemFromMeta(metaItem);
	}

	@Override
	public boolean itemExists(String vaultName, String id) {
		return dao.metaItemExists(makeTs(vaultName), id);
	}
	
	@Override
	public Map<String, RetreivalItem> getAllItems(String vaultName) {
		Map<String, MetaItem> allItems = dao.getAllMetaItems(makeTs(vaultName));
		Map<String, RetreivalItem> translatedItems = new HashMap<>();
		for (String key : allItems.keySet()) {
			translatedItems.put(key, makeRetreivalItemFromMeta(allItems.get(key)));
		}

		return translatedItems;
	}
	
	@Override
	public void remove(String vaultName, String id) {
		dao.removeMetaItem(makeTs(vaultName), id);
	}
	
	private String makeTs(String vaultName) {
		return "RetreivalManagerDao." + vaultName;
	}

	private RetreivalItem makeRetreivalItemFromMeta(MetaItem metaItem) {
		Map<String, String> attributeMap = metaItem.getMetaMap();
		RetreivalItem t = new RetreivalItem(metaItem.getId());
		t.setMetaMap(attributeMap);
		return t;
	}
}
