package com.neodem.aback.service.tracker;

import java.nio.file.attribute.BasicFileAttributes;

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
	 * associate an archiveId with a file
	 * 
	 * @param file
	 * @param archiveId
	 */
	void updateArchiveId(FileId fileId, String archiveId);

}
