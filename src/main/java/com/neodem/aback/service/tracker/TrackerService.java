package com.neodem.aback.service.tracker;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Map;

import com.neodem.aback.service.id.FileId;

public interface TrackerService {

	/**
	 * determine if we should backup this file
	 * 
	 * @param fileId
	 * @param basicFileAttributes
	 * @return
	 */
	boolean shouldBackup(FileId fileId, BasicFileAttributes basicFileAttributes);

	/**
	 * 
	 * @param fileId
	 * @param archiveId
	 * @param relativePath
	 * @param date
	 * @return true if this was a new record (eg. fileId does not exist)
	 */
	boolean updateAll(FileId fileId, String archiveId, Path relativePath, Date date);
	
	/**
	 * 
	 * @return
	 */
	Map<String, TrackerMetaItem> getAllRecords();

}
