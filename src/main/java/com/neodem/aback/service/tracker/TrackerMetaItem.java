package com.neodem.aback.service.tracker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrackerMetaItem {
	
	public static final String ARCHIVE_ID_KEY = "archiveId";
	public static final String ORIGINAL_PTH_KEY = "originalPath";
	public static final String BKD_UP_DATE_KEY = "backedUpDate";
	public static final String VAULT_NAME_KEY = "vaultName";

	private String archiveId;
	private Path originalPath;
	private Date backedUpDate;
	private String vaultName;
	
	private static DateFormat df = new SimpleDateFormat("MM-dd-YYYY hhmm");

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('"');
		b.append(originalPath);
		b.append('"');
		b.append("|");
		b.append(df.format(backedUpDate));
		b.append("|");
		b.append(vaultName);
		b.append("|");
		b.append(archiveId);
		return b.toString();
	}
	
	public TrackerMetaItem(String vaultName, Path originalPath) {
		this.originalPath = originalPath;
		this.vaultName = vaultName;
	}

	public TrackerMetaItem(String archiveId, Path originalPath, Date backedUpDate, String vaultName) {
		this(vaultName, originalPath);
		this.archiveId = archiveId;
		this.backedUpDate = backedUpDate;
	}

	public TrackerMetaItem(Map<String, String> attMap) {
		this.archiveId = attMap.get(ARCHIVE_ID_KEY);
		this.vaultName = attMap.get(VAULT_NAME_KEY);
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
		map.put(VAULT_NAME_KEY, vaultName);
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

//	public void setOriginalPath(Path originalPath) {
//		this.originalPath = originalPath;
//	}
//
//	public String getVaultName() {
//		return vaultName;
//	}
//
//	public void setVaultName(String vaultName) {
//		this.vaultName = vaultName;
//	}
}
