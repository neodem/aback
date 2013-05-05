package com.neodem.aback.service.id;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class FileId {
	
	private Path originalPath;
	
	private String hash;

	public FileId(Path file) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		originalPath = file;
		String path = file.toString();
		
		final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.reset();
		messageDigest.update(path.getBytes(Charset.forName("UTF8")));
		final byte[] resultByte = messageDigest.digest();
		hash = new String(Hex.encodeHex(resultByte));
	}

	public Path getOriginalPath() {
		return originalPath;
	}

	public String getHash() {
		return hash;
	}

}
