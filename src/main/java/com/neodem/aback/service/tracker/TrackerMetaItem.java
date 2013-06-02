package com.neodem.aback.service.tracker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.neodem.aback.aws.simpledb.MetaItem;

public class TrackerMetaItem extends MetaItem {

	public static final String ARCHIVE_ID_KEY = "archiveId";
	public static final String ORIGINAL_PTH_KEY = "originalPath";
	public static final String BKD_UP_DATE_KEY = "backedUpDate";
	public static final String VAULT_NAME_KEY = "vaultName";
	public static final String FILE_SIZE_KEY = "filesize";

	private static DateFormat df = new SimpleDateFormat("MM-dd-YYYY hhmm");

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('"');
		b.append(getOriginalPath());
		b.append('"');
		b.append("|");
		b.append(df.format(getBackedUpDate()));
		b.append("|");
		b.append(getVaultName());
		b.append("|");
		b.append(getArchiveId());
		return b.toString();
	}

	private String getVaultName() {
		return metaMap.get(VAULT_NAME_KEY);
	}

	public TrackerMetaItem(String vaultName, Path originalPath) {
		metaMap.put(VAULT_NAME_KEY, vaultName);
		metaMap.put(ORIGINAL_PTH_KEY, originalPath.toString());
	}

	public TrackerMetaItem(String archiveId, Path originalPath, Date backedUpDate, String vaultName) {
		this(vaultName, originalPath);
		metaMap.put(ARCHIVE_ID_KEY, archiveId);
		metaMap.put(BKD_UP_DATE_KEY, "" + backedUpDate.getTime());
	}

	public TrackerMetaItem() {
	}

	public void setArchiveId(String archiveId) {
		metaMap.put(ARCHIVE_ID_KEY, archiveId);
	}

	public void setBackedUpDate(Date backedUpDate) {
		metaMap.put(BKD_UP_DATE_KEY, "" + backedUpDate.getTime());
	}

	public void setFilesize(Long filesize) {
		metaMap.put(FILE_SIZE_KEY, filesize.toString());
	}

	public String getArchiveId() {
		return metaMap.get(ARCHIVE_ID_KEY);
	}

	public Date getBackedUpDate() {
		String milliString = metaMap.get(BKD_UP_DATE_KEY);
		long millis = Long.parseLong(milliString);
		Date backedUpDate = new Date();
		backedUpDate.setTime(millis);
		return backedUpDate;
	}

	public Path getOriginalPath() {
		return Paths.get(metaMap.get(ORIGINAL_PTH_KEY));
	}

	public Long getFilesize() {
		String filesizeString = metaMap.get(FILE_SIZE_KEY);
		if (filesizeString != null) {
			return new Long(filesizeString);
		}
		return null;
	}
}
