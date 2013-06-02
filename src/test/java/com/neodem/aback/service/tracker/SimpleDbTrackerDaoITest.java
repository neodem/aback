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
import com.neodem.aback.aws.simpledb.DefaultSimpleDbDao;
import com.neodem.aback.aws.simpledb.MetaItemId;
import com.neodem.aback.aws.simpledb.SimpleDbDao;
import com.neodem.aback.service.tracker.SimpleDbTrackerDao;
import com.neodem.aback.service.tracker.TrackerMetaItem;

public class SimpleDbTrackerDaoITest {
	private static final String TEST_DOMAINNAME = "DefaultTrackerDaoITestDomain";

	private static AwsSimpleDbServiceImpl db;
	private static SimpleDbDao sdao;
	private SimpleDbTrackerDao dao;
	

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
		
		DefaultSimpleDbDao d = new DefaultSimpleDbDao();
		d.setDbService(db);
		
		sdao = d;
	}

	@AfterClass
	public static void afterClass() {
		sdao = null;
		
		if (db != null) {
			db.removeDomain(TEST_DOMAINNAME);
		}
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
		
		MetaItemId fileId = new MetaItemId(originalPath);
		
		TrackerMetaItem meta = new TrackerMetaItem(vaultName, originalPath);
		meta.setArchiveId(archiveId);
		meta.setBackedUpDate(backedUpDate);
		
		dao.saveMetaItem(vaultName, fileId, meta);
		
		Thread.sleep(4000);
		
		assertThat(dao.metaItemExists(vaultName,fileId), is(true));
		
		TrackerMetaItem resultMeta = dao.getMetaItem(vaultName, fileId);
		
		assertThat(resultMeta, not(nullValue()));
		assertThat(resultMeta.getOriginalPath(), is(originalPath));
		assertThat(resultMeta.getArchiveId(), is(archiveId));
		assertThat(resultMeta.getBackedUpDate(), is(backedUpDate));
	}

}
