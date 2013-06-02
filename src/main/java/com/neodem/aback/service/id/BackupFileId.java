package com.neodem.aback.service.id;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class BackupFileId {

	private String hash;

	public BackupFileId(Path file) throws NoSuchAlgorithmException {
		hash = makeHash(file.toString());
	}
	
//	public MetaItemId(String jobId, Path path) throws NoSuchAlgorithmException {
//		hash = makeHash(jobId + path.toString());
//	}
	
	/**
	 * @param content
	 * @throws NoSuchAlgorithmException
	 */
	private String makeHash(String content) throws NoSuchAlgorithmException {
		final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.reset();
		messageDigest.update(content.getBytes(Charset.forName("UTF8")));
		final byte[] resultByte = messageDigest.digest();
		return new String(Hex.encodeHex(resultByte));
	}

	public String getHash() {
		return hash;
	}
	
	@Override
	public String toString() {
		return getHash();
	}

}
