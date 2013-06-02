package com.neodem.aback.service.id;

import java.nio.file.Path;


public interface IdService {
	public BackupFileId makeId(Path file);
}
