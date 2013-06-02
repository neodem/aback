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
import com.neodem.aback.aws.simpledb.DefaultSimpleDbDao;
import com.neodem.aback.aws.simpledb.MetaItemId;
import com.neodem.aback.aws.simpledb.SimpleDbDao;
import com.neodem.aback.service.retreival.RetreivalManager.Status;

public class SimpleDbRetreivalManagerDaoITest {
	private static final String TEST_DOMAINNAME = "SimpleDbRetreivalManagerDaoITestDomain";

	private static AwsSimpleDbServiceImpl db;
	private static SimpleDbDao sdao;
	private SimpleDbRetreivalManagerDao dao;

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
		
		RetreivalItem r = new RetreivalItem(jobId, originalPath, status);
		MetaItemId id = new MetaItemId(jobId, originalPath);
		
		dao.save(vaultName, id, r);
		
		Thread.sleep(4000);
		
		assertThat(dao.itemExists(vaultName,id), is(true));
		
		RetreivalItem resultMeta = dao.getItem(vaultName, id);
		
		assertThat(resultMeta, not(nullValue()));
		assertThat(resultMeta.getOriginalPath(), is(originalPath));
		assertThat(resultMeta.getJobId(), is(jobId));
		assertThat(resultMeta.getStatus(), is(status));
	}
}
