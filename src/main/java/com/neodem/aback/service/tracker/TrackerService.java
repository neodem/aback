package com.neodem.aback.service.tracker;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Map;

import com.neodem.aback.aws.simpledb.MetaItemId;

public interface TrackerService {

	/**
	 * determine if we should backup this file
	 * 
	 * @param fileId
	 * @param basicFileAttributes
	 * @param fileSize 
	 * 
	 * @return
	 */
	boolean shouldBackup(String vaultName, MetaItemId fileId, BasicFileAttributes basicFileAttributes, Long fileSize);

	/**
	 * update the backedUpDate on a meta item. If the item does not exist, this
	 * does nothing
	 * 
	 * @param vaultName
	 * @param id
	 * @param backedUpDate
	 */
	void updateBackedUpDate(String vaultName, MetaItemId id, Date backedUpDate);

	/**
	 * update the archiveId on a meta item. If the item does not exist, this
	 * does nothing
	 * 
	 * @param vaultName
	 * @param id
	 * @param archiveId
	 */
	void updateArchiveId(String vaultName, MetaItemId id, String archiveId);

	/**
	 * 
	 * @return
	 */
	Map<String, TrackerMetaItem> getAllRecords(String vaultName);

	/**
	 * register a new meta item. If one exists already we update the archiveId
	 * and backedUpDate (relativePath and vaultName can't be changed)
	 * 
	 * @param vaultName
	 * @param fileId
	 * @param relativePath
	 * @param archiveId
	 * @param date
	 * @param fileSize 
	 */
	void register(String vaultName, MetaItemId fileId, Path relativePath, String archiveId, Date date, Long fileSize);

}
