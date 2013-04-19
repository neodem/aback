package com.neodem.aback.service.tracker;

import java.util.Collection;
import java.util.Map;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.neodem.aback.aws.db.DB;
import com.neodem.aback.aws.db.DbUtil;
import com.neodem.aback.service.id.FileId;

public class DefaultTrackerDao implements TrackerDao {
	private DB dbService;

	@Override
	public boolean exists(FileId fileId) {
		return dbService.itemExists(fileId.getHash());
	}

	@Override
	public TrackerMetaItem getMeta(FileId fileId) {
		Item item = dbService.getItemById(fileId.getHash());
		if (item == null)
			return null;
		return makeMetaForItem(item);
	}

	@Override
	public void setMeta(FileId fileId, TrackerMetaItem meta) {
		Item item = dbService.getItemById(fileId.getHash());
		Collection<Attribute> atts = DbUtil.makeAttributeCollection(meta.getMetaMap());
		item.setAttributes(atts);
		dbService.saveItem(item);
	}

	private TrackerMetaItem makeMetaForItem(Item item) {
		Map<String, String> attMap = DbUtil.makeAttMap(item);
		return new TrackerMetaItem(attMap);
	}

	public void setDbService(DB dbService) {
		this.dbService = dbService;
	}

}
