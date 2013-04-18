package com.neodem.aback.service.tracker;

import java.nio.file.attribute.BasicFileAttributes;

import com.amazonaws.auth.AWSCredentials;
import com.neodem.aback.aws.db.DB;
import com.neodem.aback.aws.db.DefaultDBService;
import com.neodem.aback.service.id.FileId;

/**
 * @author vfumo
 *
 */
public class DefaultTrackerService implements TrackerService {
	
	private static final String DOMAINNAME = "com.neodem.aback.tracker";
	
	private TrackerDao dao;
	
	public DefaultTrackerService(AWSCredentials creds) {
		//db = new DefaultDBService(creds, DOMAINNAME);
	}

	@Override
	public boolean shouldBackup(FileId fileId, BasicFileAttributes basicFileAttributes) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateArchiveId(FileId fileId, String archiveId) {
		// TODO Auto-generated method stub
		
	}

}
