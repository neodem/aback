package com.neodem.aback.aws.simpledb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;

public class AwsSimpleDbServiceImplITest {

	private static final String TEST_DOMAINNAME = "AwsSimpleDbServiceImplITestDomain";
	private static AwsSimpleDbServiceImpl db;

	@BeforeClass
	public static void beforeClass() throws Exception {
		AWSCredentialsProvider provider = new SystemPropertiesCredentialsProvider();
		AWSCredentials creds = provider.getCredentials();

		db = new AwsSimpleDbServiceImpl();
		db.setAwsCredentials(creds);
		db.afterPropertiesSet();

		db.removeDomain(TEST_DOMAINNAME);
		db.initDomain(TEST_DOMAINNAME);
		db.setDomain(TEST_DOMAINNAME);
	}

	@AfterClass
	public static void afterClass() {
		if (db != null) {
			db.removeDomain(TEST_DOMAINNAME);
		}
	}

	@Test
	public void saveItemShouldSaveTheItemIntoAws() throws InterruptedException {
		String tablespace = "testVaultName";
		String itemId = "item1";

		AwsItem testItem = new AwsItem(itemId);
		testItem.addAttribute("att1", "att1.value");
		db.saveItem(tablespace, testItem);

		Thread.sleep(5000);

		AwsItem resultItem = db.getItem(tablespace, itemId);

		assertThat(resultItem, not(nullValue()));
		assertThat(resultItem.getId(), is(itemId));
		assertThat(resultItem.get("att1"), is("att1.value"));

		db.removeItem(tablespace, testItem);
	}

	@Test
	public void getAllShouldWork() throws InterruptedException {
		String tablespace = "testVaultName";
		String itemId = "item1";
		
		AwsItem testItem = new AwsItem(itemId);
		testItem.addAttribute("att1", "att1.value");
		db.saveItem(tablespace, testItem);
		
		Thread.sleep(5000);
		
		Collection<AwsItem> all = db.getAll(tablespace);
		
		assertThat(all.size(), is(1));
		
		db.removeItem(tablespace, testItem);
		
	}
	@Test
	public void itemExistsShouldWork() throws InterruptedException {
		String tablespace = "testVaultName";
		assertThat(db.itemExists(tablespace, "IDontexist"), is(false));

		String itemId = "IDoExist";

		AwsItem testItem = new AwsItem(itemId);
		db.saveItem(tablespace, testItem);

		Thread.sleep(5000);

		assertThat(db.itemExists(tablespace, itemId), is(true));

		db.removeItem(tablespace, testItem);

	}

}
