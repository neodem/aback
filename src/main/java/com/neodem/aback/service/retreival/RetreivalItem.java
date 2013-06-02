package com.neodem.aback.service.retreival;

import java.nio.file.Path;
import java.nio.file.Paths;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getArchiveId() == null) ? 0 : getArchiveId().hashCode());
		result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
		result = prime * result + ((isLargeFile() == null) ? 0 : isLargeFile().hashCode());
		result = prime * result + ((getOriginalPath() == null) ? 0 : getOriginalPath().hashCode());
		result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RetreivalItem other = (RetreivalItem) obj;
		if (getArchiveId() == null) {
			if (other.getArchiveId() != null)
				return false;
		} else if (!getArchiveId().equals(other.getArchiveId()))
			return false;
		if (getJobId() == null) {
			if (other.getJobId() != null)
				return false;
		} else if (!getJobId().equals(other.getJobId()))
			return false;
		if (isLargeFile() == null) {
			if (other.isLargeFile() != null)
				return false;
		} else if (!isLargeFile().equals(other.isLargeFile()))
			return false;
		if (getOriginalPath() == null) {
			if (other.getOriginalPath() != null)
				return false;
		} else if (!getOriginalPath().equals(other.getOriginalPath()))
			return false;
		if (getStatus() != other.getStatus())
			return false;
		return true;
	}

}
