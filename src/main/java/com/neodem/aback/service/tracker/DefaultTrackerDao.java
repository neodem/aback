package com.neodem.aback.service.tracker;

import com.neodem.aback.aws.db.DB;
import com.neodem.aback.service.id.FileId;

public class DefaultTrackerDao implements TrackerDao {
	private DB dbService;

	@Override
	public boolean exists(FileId fileId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TrackerMetaItem getMeta(FileId fileId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDbService(DB dbService) {
		this.dbService = dbService;
	}

	@Override
	public void setMeta(FileId fileId, TrackerMetaItem meta) {
		// TODO Auto-generated method stub
		
	}

}
