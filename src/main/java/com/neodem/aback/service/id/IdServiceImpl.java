package com.neodem.aback.service.id;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import com.neodem.aback.aws.simpledb.AwsSimpleDbService;

/**
 * will simply retrn an integer key value prefixed by a string
 * 
 * @author vfumo
 * 
 */
public class IdServiceImpl implements IdService {

//	private static final String ITEMNAME = "AwsBackedIdServiceItem";
//	private static final String LARGESTID = "LargestId";
//	private static final String KEYPREFIX = "PathItem";

	private AwsSimpleDbService dbService;

//	private Integer nextId() {
//		String largestIdString = dbService.getValue(ITEMNAME, LARGESTID);
//		if (StringUtils.isBlank(largestIdString)) {
//			largestIdString = "1";
//		}
//		Integer largestId = new Integer(largestIdString);
//		Integer nextId = largestId.intValue() + 1;
//		dbService.updateValue(ITEMNAME, LARGESTID, nextId.toString());
//
//		return nextId;
//	}

	@Override
	public FileId makeIdForPath(Path file) {
		FileId id;
		try {
			id = new FileId(file);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return id;
	}

	public void setDbService(AwsSimpleDbService dbService) {
		this.dbService = dbService;
	}
}
