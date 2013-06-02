package com.neodem.aback.service.tracker;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

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
import com.neodem.aback.aws.simpledb.dao.DefaultSimpleDbDao;
import com.neodem.aback.aws.simpledb.dao.SimpleDbDao;

public class SimpleDbTrackerDaoITest {
	private static final String TEST_DOMAINNAME = "DefaultTrackerDaoITestDomain";

	private static AwsSimpleDbServiceImpl db;
	private static SimpleDbDao sdao;
	private SimpleDbTrackerDao dao;
	
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
		
		DefaultSimpleDbDao d = new DefaultSimpleDbDao();
		d.setDbService(db);
		
		sdao = d;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (db != null) {
			AwsSimpleDbUtils.removeDomain(db.getUnderlyingDbClient(), TEST_DOMAINNAME);
		}
		
		sdao = null;
	}

	@Before
	public void setUp() throws Exception {
		dao = new SimpleDbTrackerDao();
		dao.setDao(sdao);
	}

	@After
	public void tearDown() throws Exception {
		dao = null;
	}

	@Test
	public void daoShouldSetAndGetMetaCorrectly() throws NoSuchAlgorithmException, UnsupportedEncodingException, InterruptedException {
		Path originalPath = Paths.get("C:/", "someFilename");
		String archiveId = "archiveId.value";
		Date backedUpDate = new Date();
		String vaultName = "vaultName";
		
		String itemId = "itemId";
		
		TrackerMetaItem meta = new TrackerMetaItem(itemId, vaultName, originalPath);
		meta.setArchiveId(archiveId);
		meta.setBackedUpDate(backedUpDate);
		
		dao.save(vaultName, meta);
		
		Thread.sleep(4000);
		
		assertThat(dao.exists(vaultName,itemId), is(true));
		
		TrackerMetaItem resultMeta = dao.get(vaultName, itemId);
		
		assertThat(resultMeta, not(nullValue()));
		assertThat(resultMeta.getOriginalPath(), is(originalPath));
		assertThat(resultMeta.getArchiveId(), is(archiveId));
		assertThat(resultMeta.getBackedUpDate(), is(backedUpDate));
		
		dao.remove(vaultName, itemId);
		
		Thread.sleep(4000);
				
		assertThat(dao.exists(vaultName, itemId), is(false));
	}
}
