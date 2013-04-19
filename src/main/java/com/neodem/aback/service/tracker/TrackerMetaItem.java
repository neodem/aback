package com.neodem.aback.service.tracker;

import java.nio.file.Path;
import java.util.Date;

public class TrackerMetaItem {

	private String archiveId;
	private Path originalPath;
	private Date backedUpDate;

	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}

	public String getArchiveId() {
		return archiveId;
	}

	public void setBackedUpDate(Date date) {
		this.backedUpDate = date;
	}

	public Date getBackedUpDate() {
		return backedUpDate;
	}

	public Path getOriginalPath() {
		return originalPath;
	}

	public void setOriginalPath(Path originalPath) {
		this.originalPath = originalPath;
	}

}
