package com.neodem.aback.aws.simpledb.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.neodem.aback.aws.simpledb.AwsSimpleDbServiceImpl;
import com.neodem.aback.aws.simpledb.AwsSimpleDbUtils;

public class DefaultSimpleDbDaoITest {
	
	private static final String TEST_DOMAINNAME = "DefaultSimpleDbDaoITestDomain";

	private static AwsSimpleDbServiceImpl db;
	private DefaultSimpleDbDao dao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		AWSCredentialsProvider provider = new SystemPropertiesCredentialsProvider();
		AWSCredentials creds = provider.getCredentials();

		db = new AwsSimpleDbServiceImpl();
		db.setAwsCredentials(creds);
		db.setDomain(TEST_DOMAINNAME);
		db.init();
		
		AwsSimpleDbUtils.removeDomain(db.getUnderlyingDbClient(), TEST_DOMAINNAME);
		AwsSimpleDbUtils.initDomain(db.getUnderlyingDbClient(), TEST_DOMAINNAME);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (db != null) {
			AwsSimpleDbUtils.removeDomain(db.getUnderlyingDbClient(), TEST_DOMAINNAME);
		}
	}

	@Before
	public void setUp() throws Exception {
		dao = new DefaultSimpleDbDao();
		dao.setDbService(db);
	}

	@After
	public void tearDown() throws Exception {
		dao = null;
	}

	@Test(expected = IllegalArgumentException.class)  
	public void saveMetaItemShouldFailIfNoIdSet() {
		String tablespace = "testTablespace";
		MetaItem item = new MetaItem(null);
		
		dao.saveMetaItem(tablespace, item);
	}
	
	@Test 
	public void saveMetaItemShouldWork() throws InterruptedException {
		String tablespace = "testTablespace";
		String itemId = "itemId";
		MetaItem item = new MetaItem(itemId);
		
		item.addMeta("key", "value");
		dao.saveMetaItem(tablespace, item);

		Thread.sleep(5000);
		
		MetaItem recoveredItem = dao.getMetaItem(tablespace, itemId);
		
		assertThat(recoveredItem, not(nullValue()));
		assertThat(recoveredItem.getId(), is(itemId));
		assertThat(recoveredItem.getMetaMap().get("key"), is("value"));

		dao.removeMetaItem(tablespace, itemId);
	}
	
	@Test
	public void getAllMetaItemShouldWork() throws InterruptedException {
		String tablespace = "testTablespace";
		String itemId = "itemId";
		MetaItem item = new MetaItem(itemId);
		
		item.addMeta("key", "value");
		dao.saveMetaItem(tablespace, item);

		Thread.sleep(5000);
		
		Map<String, MetaItem> all = dao.getAllMetaItems(tablespace);
		
		assertThat(all.size(), is(1));
		assertThat(all.get(itemId), is(item));
		
		dao.removeMetaItem(tablespace, itemId);
	}
	
	@Test
	public void metaItemExistsShouldWork() throws InterruptedException {
		String tablespace = "testVaultName";
		assertThat(dao.metaItemExists(tablespace, "IDontexist"), is(false));

		String itemId = "IDoExist";

		MetaItem item = new MetaItem(itemId);
		dao.saveMetaItem(tablespace, item);

		Thread.sleep(5000);

		assertThat(dao.metaItemExists(tablespace, itemId), is(true));

		dao.removeMetaItem(tablespace, itemId);
	}
	
	@Test
	public void removeMetaItemShouldWork() throws InterruptedException {
		String tablespace = "testTablespace";
		String itemId = "itemId";
		MetaItem item = new MetaItem(itemId);
		
		dao.saveMetaItem(tablespace, item);

		Thread.sleep(5000);
		
		dao.removeMetaItem(tablespace, itemId);
		
		assertThat(dao.metaItemExists(tablespace, itemId), is(false));
	}

}
