package com.neodem.aback.service.tracker;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.Map;

import com.neodem.aback.service.id.MetaItemId;

/**
 * @author vfumo
 * 
 */
public class DefaultTrackerService implements TrackerService {

	private TrackerDao trackerDao;

	@Override
	public boolean shouldBackup(String vaultName, MetaItemId fileId, BasicFileAttributes basicFileAttributes) {
		if (!trackerDao.exists(vaultName, fileId)) {
			return true;
		}

		TrackerMetaItem meta = trackerDao.getMetaItem(vaultName, fileId);
		return determineBasedOnMeta(meta, basicFileAttributes);
	}

	@Override
	public void updateBackedUpDate(String vaultName, MetaItemId id, Date backedUpDate) {
		TrackerMetaItem meta = trackerDao.getMetaItem(vaultName, id);
		if (meta != null) {
			meta.setBackedUpDate(backedUpDate);
			trackerDao.saveMetaItem(vaultName, id, meta);
		}
	}

	@Override
	public void updateArchiveId(String vaultName, MetaItemId id, String archiveId) {
		TrackerMetaItem meta = trackerDao.getMetaItem(vaultName, id);
		if (meta != null) {
			meta.setArchiveId(archiveId);
			trackerDao.saveMetaItem(vaultName, id, meta);
		}
	}

	@Override
	public Map<String, TrackerMetaItem> getAllRecords(String vaultName) {
		return trackerDao.getAllItems(vaultName);
	}

	@Override
	public void register(String vaultName, MetaItemId fileId, Path path, String archiveId, Date date) {

		TrackerMetaItem meta = null;

		if (trackerDao.exists(vaultName, fileId)) {
			meta = trackerDao.getMetaItem(vaultName, fileId);
		} else {
			meta = new TrackerMetaItem(vaultName, path);
		}

		meta.setArchiveId(archiveId);
		meta.setBackedUpDate(date);

		trackerDao.saveMetaItem(vaultName, fileId, meta);
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
