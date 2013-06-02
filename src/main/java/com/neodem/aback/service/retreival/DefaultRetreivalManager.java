package com.neodem.aback.service.retreival;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.neodem.aback.aws.simpledb.MetaItemId;

public class DefaultRetreivalManager implements RetreivalManager {
	
	private RetreivalManagerDao retreivalManagerDao;

	@Override
	public void addRetrievialItem(String vaultName, MetaItemId id, RetreivalItem r) {
		retreivalManagerDao.save(vaultName, id, r);
	}

	@Required
	public void setRetreivalManagerDao(RetreivalManagerDao retreivalManagerDao) {
		this.retreivalManagerDao = retreivalManagerDao;
	}

	@Override
	public Map<String, RetreivalItem> getAllRecords(String vaultName) {
		return retreivalManagerDao.getAllItems(vaultName);
	}

	@Override
	public boolean exists(String vaultName, MetaItemId id) {
		return retreivalManagerDao.itemExists(vaultName, id);
	}

}
