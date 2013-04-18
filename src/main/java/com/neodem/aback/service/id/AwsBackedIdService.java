package com.neodem.aback.service.id;

import java.nio.file.Path;

import org.apache.commons.lang.StringUtils;

import com.amazonaws.auth.AWSCredentials;
import com.neodem.aback.aws.db.DB;
import com.neodem.aback.aws.db.DefaultDBService;

/**
 * will simply retrn an integer key value prefixed by a string
 * 
 * @author vfumo
 * 
 */
public class AwsBackedIdService implements IdService {
	
	private static final String DOMAINNAME = "com.neodem.aback.idService";

	private static final String ITEMNAME = "AwsBackedIdServiceItem";
	private static final String LARGESTID = "LargestId";
	private static final String KEYPREFIX = "PathItem";

	private DB db;

	public AwsBackedIdService(AWSCredentials creds) {
		db = new DefaultDBService(creds, DOMAINNAME);
	}

	public String makeIdForPath(Path path) {
		String id = nextId().toString();
		return KEYPREFIX + id;
	}

	private Integer nextId() {
		String largestIdString = db.getValue(ITEMNAME, LARGESTID);
		if (StringUtils.isBlank(largestIdString)) {
			largestIdString = "1";
		}
		Integer largestId = new Integer(largestIdString);
		Integer nextId = largestId.intValue() + 1;
		db.updateValue(ITEMNAME, LARGESTID, nextId.toString());

		return nextId;
	}

	@Override
	public FileId makeIdForPath(Path sourceRoot, Path file) {
		// TODO Auto-generated method stub
		return null;
	}
}
