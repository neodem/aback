package com.neodem.aback.service.tracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.neodem.aback.aws.simpledb.MetaItemId;

/**
 * @author vfumo
 * 
 */
public class DefaultTrackerService implements TrackerService {
	private static Logger log = Logger.getLogger(DefaultTrackerService.class);
	
	private TrackerDao trackerDao;
	private Path trackerFilePath;

	@Override
	public boolean shouldBackup(String vaultName, MetaItemId fileId, BasicFileAttributes basicFileAttributes, Long filesize) {
		// we don't have  record? Then def back this up!
		if (!trackerDao.metaItemExists(vaultName, fileId)) {
			return true;
		}

		TrackerMetaItem meta = trackerDao.getMetaItem(vaultName, fileId);
		return determineBasedOnMeta(meta, basicFileAttributes, filesize);
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
		return trackerDao.getAllTrackerItems(vaultName);
	}

	@Override
	public void register(String vaultName, MetaItemId fileId, Path path, String archiveId, Date date, Long filesize) {

		try {
			writeToFile(vaultName, fileId, path, archiveId, date);
		} catch (IOException e) {
			log.error("could not write to file : " + e.getMessage());
		}

		TrackerMetaItem meta = null;

		if (trackerDao.metaItemExists(vaultName, fileId)) {
			meta = trackerDao.getMetaItem(vaultName, fileId);
		} else {
			meta = new TrackerMetaItem(vaultName, path);
		}

		meta.setArchiveId(archiveId);
		meta.setBackedUpDate(date);
		meta.setFilesize(filesize);

		trackerDao.saveMetaItem(vaultName, fileId, meta);
	}

	private boolean determineBasedOnMeta(TrackerMetaItem meta, BasicFileAttributes atts, Long filesize) {
		FileTime lastModifiedTime = atts.lastModifiedTime();
		long lastModified = lastModifiedTime.toMillis();

		Date backedUpDate = meta.getBackedUpDate();
		long backed = backedUpDate.getTime();

		return lastModified > backed;
	}
	
	private static final DateFormat df = new SimpleDateFormat("yyMMddHHmmssZ");
	private void writeToFile(String vaultName, MetaItemId fileId, Path path, String archiveId, Date date) throws IOException {
		String content = String.format("%s|%s|%s|%s|%s\n", df.format(date), vaultName, fileId.getHash(), path, archiveId);
		log.info(content);
		Files.write(trackerFilePath, content.getBytes(), StandardOpenOption.APPEND);
	}

	@Required
	public void setTrackerDao(TrackerDao trackerDao) {
		this.trackerDao = trackerDao;
	}

	@Required
	public void setTrackerFilePath(String trackerFilePath) {
		this.trackerFilePath = Paths.get(trackerFilePath);
		if(!Files.exists(this.trackerFilePath)) {
			try {
				Files.createFile(this.trackerFilePath);
			} catch (IOException e) {
				log.warn("could not create file : " + e.getMessage());
			}
		}
	}

}
