package com.neodem.aback.service.retreival;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

public class DefaultRetreivalManager implements RetreivalManager {

	private RetreivalManagerDao retreivalManagerDao;

	@Required
	public void setRetreivalManagerDao(RetreivalManagerDao retreivalManagerDao) {
		this.retreivalManagerDao = retreivalManagerDao;
	}

	@Override
	public Map<String, RetreivalItem> getAllRetreivalItems(String vaultName) {
		return retreivalManagerDao.getAllItems(vaultName);
	}

	@Override
	public void addRetreivalItem(String vaultName, RetreivalItem r) {
		retreivalManagerDao.save(vaultName, r);
	}

	@Override
	public boolean retreivalItemExists(String vaultName, String id) {
		return retreivalManagerDao.itemExists(vaultName, id);
	}

	@Override
	public void removeRetreivalItem(String vaultName, RetreivalItem r) {
		retreivalManagerDao.remove(vaultName, r.getId());
	}

}
