package com.neodem.aback.aws.simpledb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

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
	}

	@AfterClass
	public static void afterClass() {
		if (db != null) {
			db.removeDomain(TEST_DOMAINNAME);
		}
	}

	@Test
	public void saveItemShouldSaveTheItemIntoAws() throws InterruptedException {
		String itemId = "item1";

		Item testItem = new Item(itemId);
		testItem.put("att1", "att1.value");
		db.saveItem(testItem);

		Thread.sleep(5000);

		Item resultItem = db.getItem(itemId);

		assertThat(resultItem, not(nullValue()));
		assertThat(resultItem.getId(), is(itemId));
		assertThat(resultItem.get("att1"), is("att1.value"));

		db.removeItem(testItem);
	}

	@Test
	public void itemExistsShouldWork() throws InterruptedException {
		assertThat(db.itemExists("IDontexist"), is(false));

		String itemId = "IDoExist";

		Item testItem = new Item(itemId);
		db.saveItem(testItem);
		
		Thread.sleep(5000);

		assertThat(db.itemExists(itemId), is(true));

		db.removeItem(testItem);

	}

}
