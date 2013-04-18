package com.neodem.aback.service.scanner;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class FileVisitor extends SimpleFileVisitor<Path> {

	private Map<Path, BasicFileAttributes> files;

	public FileVisitor() {
		files = new HashMap<Path, BasicFileAttributes>();
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		files.put(file, attrs);
		return FileVisitResult.CONTINUE;
	}

	public Map<Path, BasicFileAttributes> getFiles() {
		return files;
	}

}
