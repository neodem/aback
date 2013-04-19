package com.neodem.aback.service.tracker;

import com.neodem.aback.service.id.FileId;

public interface TrackerDao {

	/**
	 * return if a fileId record exists.
	 * 
	 * @param fileId
	 * @return
	 */
	boolean exists(FileId fileId);

	TrackerMetaItem getMeta(FileId fileId);

	void setMeta(FileId fileId, TrackerMetaItem meta);

}
