package com.neodem.aback.service.tracker;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

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

	void updateAll(FileId fileId, String archiveId, Path relativePath, Date date);

}
