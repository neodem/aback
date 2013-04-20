package com.neodem.aback.aws.service.tracker;

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
import com.neodem.aback.service.id.FileId;
import com.neodem.aback.service.tracker.DefaultTrackerDao;
import com.neodem.aback.service.tracker.TrackerMetaItem;

public class DefaultTrackerDaoITest {
	private static final String TEST_DOMAINNAME = "DefaultTrackerDaoITestDomain";

	private static AwsSimpleDbServiceImpl db;
	private DefaultTrackerDao dao;

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

	@Before
	public void setUp() throws Exception {
		dao = new DefaultTrackerDao();
		dao.setDbService(db);
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
		
		FileId fileId = new FileId(originalPath);
		
		TrackerMetaItem meta = new TrackerMetaItem();
		meta.setArchiveId(archiveId);
		meta.setBackedUpDate(backedUpDate);
		meta.setOriginalPath(originalPath);
		
		dao.setMeta(fileId, meta);
		
		Thread.sleep(4000);
		
		assertThat(dao.exists(fileId), is(true));
		
		TrackerMetaItem resultMeta = dao.getMeta(fileId);
		
		assertThat(resultMeta, not(nullValue()));
		assertThat(resultMeta.getOriginalPath(), is(originalPath));
		assertThat(resultMeta.getArchiveId(), is(archiveId));
		assertThat(resultMeta.getBackedUpDate(), is(backedUpDate));
	}

}
