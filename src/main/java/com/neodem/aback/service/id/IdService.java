package com.neodem.aback.service.id;

import java.nio.file.Path;

public interface IdService {
	public MetaItemId makeId(Path file);
}
