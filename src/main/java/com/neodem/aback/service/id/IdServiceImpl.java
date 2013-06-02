package com.neodem.aback.service.id;

import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;


/**
 * @author vfumo
 * 
 */
public class IdServiceImpl implements IdService {

	@Override
	public BackupFileId makeId(Path file) {
		BackupFileId id;
		try {
			id = new BackupFileId(file);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return id;
	}
}
