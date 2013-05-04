package com.neodem.aback.service.scanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Map.Entry;

public class InMemoryScanner implements ScannerService {

	public Map<Path, BasicFileAttributes> scan(Path startDir) {
		FileVisitor fv = new FileVisitor();
		
		try {
			Files.walkFileTree(startDir, fv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fv.getFiles();
	}

	public static void main(String[] args) {
		InMemoryScanner s = new InMemoryScanner();
		Path startPath = Paths.get("m:/");
		Map<Path, BasicFileAttributes> scan = s.scan(startPath);
		Entry<Path, BasicFileAttributes> next = scan.entrySet().iterator().next();
		
		Path p = next.getKey();
		BasicFileAttributes b = next.getValue();
		
		System.out.println(p);
		System.out.println(b.lastModifiedTime());
	}

}
