package com.neodem.aback.service.id;

import java.nio.file.Path;

import com.neodem.aback.aws.simpledb.MetaItemId;

public interface IdService {
	public MetaItemId makeId(Path file);
}
