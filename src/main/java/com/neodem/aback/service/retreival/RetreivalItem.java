package com.neodem.aback.service.retreival;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.neodem.aback.aws.simpledb.dao.MetaItem;
import com.neodem.aback.service.retreival.RetreivalManager.Status;

public class RetreivalItem extends MetaItem {
	public static final String JOB_ID_KEY = "jobId";
	public static final String ARCHIVE_ID_KEY = "archiveId";
	public static final String ORIGINAL_PTH_KEY = "originalPath";
	public static final String STATUS_KEY = "status";
	public static final String LARGE_KEY = "largeFile";

	public RetreivalItem(String retreivalItemId, String jobId, String archiveId, Path originalPath, Status status, Boolean largeFile) {
		super(retreivalItemId);
		setJobId(jobId);
		setOriginalPath(originalPath);
		setStatus(status);
		setArchiveId(archiveId);
		setLargeFile(largeFile);
	}

	public RetreivalItem(String id) {
		super(id);
	}

	public void setLargeFile(Boolean largeFile) {
		metaMap.put(LARGE_KEY, largeFile.toString());
	}

	public Boolean isLargeFile() {
		return new Boolean(metaMap.get(LARGE_KEY));
	}

	public void setArchiveId(String archiveId) {
		metaMap.put(ARCHIVE_ID_KEY, archiveId);
	}

	public String getArchiveId() {
		return metaMap.get(ARCHIVE_ID_KEY);
	}

	public String getJobId() {
		return metaMap.get(JOB_ID_KEY);
	}

	public void setJobId(String jobId) {
		metaMap.put(JOB_ID_KEY, jobId);
	}

	public Path getOriginalPath() {
		return Paths.get(metaMap.get(ORIGINAL_PTH_KEY));
	}

	public void setOriginalPath(Path originalPath) {
		metaMap.put(ORIGINAL_PTH_KEY, originalPath.toString());
	}

	public Status getStatus() {
		return Status.valueOf(metaMap.get(STATUS_KEY));
	}

	public void setStatus(Status status) {
		metaMap.put(STATUS_KEY, status.name());
	}

	@Override
	public String toString() {
		return "RetreivalItem [jobId=" + getJobId() + ", archiveId=" + getArchiveId() + ", originalPath=" + getOriginalPath() + ", status=" + getStatus()
				+ ", largeFile=" + isLargeFile() + "]";
	}

	public int hashCode() {
		return new HashCodeBuilder(139, 457).appendSuper(super.hashCode()).append(getJobId()).append(getArchiveId()).append(getOriginalPath())
				.append(getStatus()).append(isLargeFile()).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		RetreivalItem rhs = (RetreivalItem) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(getJobId(), rhs.getJobId()).append(getArchiveId(), rhs.getArchiveId())
				.append(getOriginalPath(), rhs.getOriginalPath()).append(getStatus(), rhs.getStatus()).append(isLargeFile(), rhs.isLargeFile()).isEquals();
	}

}
