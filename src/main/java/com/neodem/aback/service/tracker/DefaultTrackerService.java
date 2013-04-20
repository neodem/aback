package com.neodem.aback.service.tracker;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;

import com.neodem.aback.service.id.FileId;

/**
 * @author vfumo
 *
 */
public class DefaultTrackerService implements TrackerService {
	
	private TrackerDao trackerDao;
	
	@Override
	public boolean shouldBackup(FileId fileId, BasicFileAttributes basicFileAttributes) {
		if(!trackerDao.exists(fileId)) {
			return true;
		}
		
		TrackerMetaItem meta = trackerDao.getMeta(fileId);
		return determineBasedOnMeta(meta, basicFileAttributes);
	}
	
	@Override
	public void updateAll(FileId fileId, String archiveId, Path relativePath, Date date) {
		TrackerMetaItem meta = trackerDao.getMeta(fileId);
		meta.setArchiveId(archiveId);
		meta.setBackedUpDate(date);
		meta.setOriginalPath(relativePath);
		trackerDao.setMeta(fileId, meta);
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
}
