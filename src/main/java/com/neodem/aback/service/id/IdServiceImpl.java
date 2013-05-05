package com.neodem.aback.service.id;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

/**
 * will simply return an integer key value prefixed by a string
 * 
 * @author vfumo
 * 
 */
public class IdServiceImpl implements IdService {

	@Override
	public MetaItemId makeId(Path file) {
		MetaItemId id;
		try {
			id = new MetaItemId(file);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return id;
	}
}
