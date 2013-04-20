package com.neodem.aback.service.tracker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrackerMetaItem {
	
	public static final String ARCHIVE_ID_KEY = "archiveId";
	public static final String ORIGINAL_PTH_KEY = "originalPath";
	public static final String BKD_UP_DATE_KEY = "backedUpDate";

	private String archiveId;
	private Path originalPath;
	private Date backedUpDate;

	public TrackerMetaItem() {
	}

	public TrackerMetaItem(String archiveId, Path originalPath, Date backedUpDate) {
		super();
		this.archiveId = archiveId;
		this.originalPath = originalPath;
		this.backedUpDate = backedUpDate;
	}

	public TrackerMetaItem(Map<String, String> attMap) {
		this.archiveId = attMap.get(ARCHIVE_ID_KEY);
		this.originalPath = Paths.get(attMap.get(ORIGINAL_PTH_KEY));
		
		String milliString = attMap.get(BKD_UP_DATE_KEY);
		long millis = Long.parseLong(milliString);
		Date backedUpDate = new Date();
		backedUpDate.setTime(millis);
		
		this.backedUpDate = backedUpDate;
	}
	
	public Map<String, String> getMetaMap() {
		Map<String,String> map = new HashMap<String,String>(3);
		
		map.put(ARCHIVE_ID_KEY, archiveId);
		map.put(ORIGINAL_PTH_KEY, originalPath.toString());
		map.put(BKD_UP_DATE_KEY, "" + backedUpDate.getTime());
		
		return map;
	}

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
