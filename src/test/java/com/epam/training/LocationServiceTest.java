package com.epam.training;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import com.epam.training.dao.LocationDao;
import com.epam.training.dao.jdbc.SimpleJdbcLocationEntityImpl;
import com.epam.training.dao.url.UrlMyIpDao;
import com.epam.training.domain.LocationEntity;
import com.epam.training.exception.AppException;
import com.epam.training.exception.ErrorInfoFactory;
import com.epam.training.service.impl.LocationServiceImpl;
import com.epam.training.utils.Ip4Converter;
import com.epam.training.utils.Validator;

public class LocationServiceTest {

	/** Instance of tested UNIT */
	private LocationServiceImpl locationServiceImpl;

	/** MOCK classes emulating used services */
	private LocationDao locationDao;

	private UrlMyIpDao urlMyIpDao;

	private Ip4Converter ipConverter;

	private Validator ipValidator;

	/** Constants */
	private static final String INVALID_IP4_ADDRESS = "256.256.0.3";
	private static final String VALID_IP4_ADDRESS = "192.168.100.199";

	private static final double CONVERTED_ADDRESS = 9999.99;

	@Before
	public void setup() {

		// instantiate the unit under test:
		locationServiceImpl = new LocationServiceImpl();

		// create a mock instances
		locationDao = createMock(SimpleJdbcLocationEntityImpl.class);
		urlMyIpDao = createMock(UrlMyIpDao.class);
		ipConverter = createMock(Ip4Converter.class);
		ipValidator = createMock(Validator.class);

		// inject the unit's dependencies as mock object via reflection
		ReflectionTestUtils.setField(locationServiceImpl, "locationDao",
				locationDao);
		ReflectionTestUtils.setField(locationServiceImpl, "urlMyIpDao",
				urlMyIpDao);
		ReflectionTestUtils.setField(locationServiceImpl, "ipConverter",
				ipConverter);
		ReflectionTestUtils.setField(locationServiceImpl, "ipValidator",
				ipValidator);
	}

	private LocationEntity getDefaultLocationEntity() {
		LocationEntity locationEntity = new LocationEntity();

		locationEntity.setCityName("BUDAPEST");
		locationEntity.setCountryCode("HU");
		locationEntity.setCountryName("HUNGARY");
		locationEntity.setIp4Address("127.0.0.1");
		locationEntity.setLatitude(10.0);
		locationEntity.setLongitude(5.0);
		locationEntity.setRegionName("BUDAPEST");
		locationEntity.setTimeZone("+5:00");
		locationEntity.setZipCode("1054");

		return locationEntity;
	}

	/**
	 * Test case: call location service with valid ip.
	 * 
	 * Expected result:
	 * <ul>
	 * <li>Service is invoked properly and returns a LocationEntity object</li>
	 * <li>Service is not allowed to return with an exception.</li>
	 * </ul>
	 */
	@Test
	public void testGetLocation_ValidIp() {
		// mock return value
		LocationEntity mockResponse_LocationEntity = getDefaultLocationEntity();

		AppException receivedException = null;
		LocationEntity response_LocationEntity = null;
		try {
			// mock calls
			expect(ipValidator.validateIp4String(VALID_IP4_ADDRESS)).andReturn(
					true);
			replay(ipValidator);

			expect(ipConverter.convertFromStringToDouble(VALID_IP4_ADDRESS))
					.andReturn(CONVERTED_ADDRESS);
			replay(ipConverter);

			expect(locationDao.find(CONVERTED_ADDRESS)).andReturn(
					mockResponse_LocationEntity);
			replay(locationDao);

			// service method call
			response_LocationEntity = locationServiceImpl
					.getLocation(VALID_IP4_ADDRESS);

			verify(ipValidator);
			verify(ipConverter);
			verify(locationDao);
		} catch (AppException _ae) {
			receivedException = _ae;
		}

		Assert.notNull(response_LocationEntity,
				"service must return a LocationEntity which is not null");
		Assert.isNull(receivedException,
				"service is not allowed to return an exception right now");
	}

	/**
	 * Test case: call location service with invalid ip.
	 * 
	 * Expected result:
	 * <ul>
	 * <li>Service is invoked properly and an exception is thrown.</li>
	 * <li>Service is not allowed to return with a LocationObject.</li>
	 * </ul>
	 */
	@Test
	public void testGetLocation_InValidIp() {
		AppException receivedException = null;
		LocationEntity response_LocationEntity = null;
		try {
			// mock calls
			expect(ipValidator.validateIp4String(INVALID_IP4_ADDRESS))
					.andReturn(false);
			replay(ipValidator);

			// service method call
			response_LocationEntity = locationServiceImpl
					.getLocation(INVALID_IP4_ADDRESS);

			verify(ipValidator);
		} catch (AppException _ae) {
			receivedException = _ae;
		}

		Assert.isNull(response_LocationEntity,
				"service is not allowed to return a LocationEntity right now");
		Assert.notNull(receivedException,
				"service must return an exception right now");
	}

	/**
	 * Test case: call location service, dao service is down.
	 * 
	 * Expected result:
	 * <ul>
	 * <li>Service is invoked properly and an exception is thrown</li>
	 * <li>Service is not allowed to return with a LocationObject.</li>
	 * </ul>
	 */
	@Test
	public void testGetLocation_DaoServiceDown() {
		// mock return value
		AppException mockThrow_JdbcDaoError = new AppException();
		mockThrow_JdbcDaoError.addInfo(ErrorInfoFactory.getJdbcDaoErrorInfo(
				"LocationServiceTest", "jdbc service is down", null));

		AppException receivedException = null;
		LocationEntity response_LocationEntity = null;
		try {
			// mock calls
			expect(ipValidator.validateIp4String(VALID_IP4_ADDRESS)).andReturn(
					true);
			replay(ipValidator);

			expect(ipConverter.convertFromStringToDouble(VALID_IP4_ADDRESS))
					.andReturn(CONVERTED_ADDRESS);
			replay(ipConverter);

			expect(locationDao.find(CONVERTED_ADDRESS)).andThrow(
					mockThrow_JdbcDaoError);
			replay(locationDao);

			// service method call
			response_LocationEntity = locationServiceImpl
					.getLocation(VALID_IP4_ADDRESS);

			verify(ipValidator);
			verify(ipConverter);
			verify(locationDao);
		} catch (AppException _ae) {
			receivedException = _ae;
		}

		Assert.isNull(response_LocationEntity,
				"service is not allowed to return a LocationEntity which is not null");
		Assert.notNull(receivedException,
				"service must return an exception right now");
	}

	/**
	 * Test case: call ip address service with fine conditions
	 * 
	 * Expected result:
	 * <ul>
	 * <li>Service is invoked properly and return with client wan ip address</li>
	 * <li>Service must return with an exception.</li>
	 * </ul>
	 */
	@Test
	public void testMyIpAddress_Success() {
		// mock return value
		String mockResponse_IpAddress = "192.168.100.199";
		AppException receivedException = null;

		String response_IpAddress = null;
		try {
			// mock calls
			expect(urlMyIpDao.find()).andReturn(mockResponse_IpAddress);
			replay(urlMyIpDao);

			// service method call
			response_IpAddress = locationServiceImpl.getMyIpAddress();

			verify(urlMyIpDao);
		} catch (AppException _ae) {
			receivedException = _ae;
		}

		Assert.notNull(response_IpAddress,
				"service must return an IpAddress right now");
		Assert.isNull(receivedException,
				"service is not allowed to return an exception right now");
	}

	/**
	 * Test case: call ip address service and dao service is down 
	 * 
	 * Expected result:
	 * <ul>
	 * <li>Service is invoked properly and throws exception</li>
	 * <li>Service is not allowed to return with ip address.</li>
	 * </ul>
	 */
	@Test
	public void testMyIpAddress_DaoServiceDown() {
		// mock return value
		AppException mockThrow_UrlDaoError = new AppException();
		mockThrow_UrlDaoError.addInfo(ErrorInfoFactory.getUrlDaoErrorInfo(
				"LocationServiceTest", "url service is down", null));

		AppException receivedException = null;
		String response_IpAddress = null;

		try {
			// mock calls
			expect(urlMyIpDao.find()).andThrow(mockThrow_UrlDaoError);
			replay(urlMyIpDao);

			// service method call
			response_IpAddress = locationServiceImpl.getMyIpAddress();

			verify(urlMyIpDao);
		} catch (AppException _ae) {
			receivedException = _ae;
		}

		Assert.isNull(response_IpAddress,
				"service is not allowed to return an IpAddress right now");
		Assert.notNull(receivedException,
				"service must return an exception right now");
	}
}
