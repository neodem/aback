package com.neodem.aback.service.tracker;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.Map;

import com.neodem.aback.service.id.FileId;

/**
 * @author vfumo
 * 
 */
public class DefaultTrackerService implements TrackerService {

	private TrackerDao trackerDao;

	@Override
	public boolean shouldBackup(FileId fileId, BasicFileAttributes basicFileAttributes) {
		if (!trackerDao.exists(fileId)) {
			return true;
		}

		TrackerMetaItem meta = trackerDao.getMeta(fileId);
		return determineBasedOnMeta(meta, basicFileAttributes);
	}

	@Override
	public boolean updateAll(FileId fileId, String archiveId, Path relativePath, Date date) {
		boolean newItem = true;
		
		TrackerMetaItem meta = null;

		if (trackerDao.exists(fileId)) {
			newItem = false;
			meta = trackerDao.getMeta(fileId);
		} else {
			meta = new TrackerMetaItem();
		}

		meta.setArchiveId(archiveId);
		meta.setBackedUpDate(date);
		meta.setOriginalPath(relativePath);
		trackerDao.setMeta(fileId, meta);
		
		return newItem;
	}

	private boolean determineBasedOnMeta(TrackerMetaItem meta, BasicFileAttributes atts) {
		FileTime lastModifiedTime = atts.lastModifiedTime();
		long lastModified = lastModifiedTime.toMillis();

		Date backedUpDate = meta.getBackedUpDate();
		long backed = backedUpDate.getTime();

		return lastModified > backed;
	}

	public void setTrackerDao(TrackerDao trackerDao) {
		this.trackerDao = trackerDao;
	}

	@Override
	public Map<String, TrackerMetaItem> getAllRecords() {
		return trackerDao.getAllRecords();
	}
}
