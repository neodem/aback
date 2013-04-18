package com.neodem.aback.service.scanner;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

public interface ScannerService {
	public Map<Path, BasicFileAttributes> scan(Path startDir);
}
