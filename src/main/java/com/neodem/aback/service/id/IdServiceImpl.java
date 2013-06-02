package com.neodem.aback.service.id;

import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import com.neodem.aback.aws.simpledb.MetaItemId;

/**
 * @author vfumo
 * 
 */
public class IdServiceImpl implements IdService {

	@Override
	public MetaItemId makeId(Path file) {
		MetaItemId id;
		try {
			id = new MetaItemId(file);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return id;
	}
}
