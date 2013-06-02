package com.neodem.aback.service.retreival;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

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
import com.neodem.aback.service.retreival.RetreivalManager.Status;

public class SimpleDbRetreivalManagerDaoITest {
	private static final String TEST_DOMAINNAME = "SimpleDbRetreivalManagerDaoITestDomain";

	private static AwsSimpleDbServiceImpl db;
	private static SimpleDbDao sdao;
	private SimpleDbRetreivalManagerDao dao;

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
		dao = new SimpleDbRetreivalManagerDao();
		dao.setDao(sdao);
	}

	@After
	public void tearDown() throws Exception {
		dao = null;
	}

	@Test
	public void daoShouldSetAndGetMetaCorrectly() throws NoSuchAlgorithmException, UnsupportedEncodingException, InterruptedException {
		String jobId = "jobId";
		Path originalPath = Paths.get("C:/", "someFilename");
		Status status = Status.Started;
		String vaultName = "vaultName";
		String archiveId = "archiveId";
		Boolean largeFile = true;
		String itemId = "itemId";

		RetreivalItem r = new RetreivalItem(itemId, jobId, archiveId, originalPath, status, largeFile);

		dao.save(vaultName, r);

		Thread.sleep(4000);

		assertThat(dao.itemExists(vaultName, itemId), is(true));

		RetreivalItem resultMeta = dao.getItem(vaultName, itemId);

		assertThat(resultMeta, not(nullValue()));
		assertThat(resultMeta.getOriginalPath(), is(originalPath));
		assertThat(resultMeta.getJobId(), is(jobId));
		assertThat(resultMeta.getStatus(), is(status));
		assertThat(resultMeta.getArchiveId(), is(archiveId));
		assertThat(resultMeta.isLargeFile(), is(true));

		dao.remove(vaultName, itemId);
		
		Thread.sleep(4000);
				
		assertThat(dao.itemExists(vaultName, itemId), is(false));
	}
}
