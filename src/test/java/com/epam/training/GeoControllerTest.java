package com.epam.training;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.support.BindingAwareModelMap;

import com.epam.training.domain.LocationEntity;
import com.epam.training.exception.AppException;
import com.epam.training.exception.ErrorInfoFactory;
import com.epam.training.service.LocationService;
import com.epam.training.service.impl.LocationServiceImpl;
import com.epam.training.web.GeoController;

public class GeoControllerTest {

	/** Instance of tested UNIT */
	private GeoController geoController;

	/** MOCK classes emulating used services */
	private LocationService locationService;

	/** Constants */
	private static final String INVALID_IP4_ADDRESS = "256.256.0.3";
	private static final String VALID_IP4_ADDRESS = "192.168.100.199";
	private static final String EMPTY_STRING = "";
	private static final String MY_ADDRESS = "192.168.100.199";

	private static final Locale LOCALE = Locale.ENGLISH;

	@Before
	public void setup() {

		// instantiate the unit under test:
		geoController = new GeoController();

		// create a mock instances
		locationService = createMock(LocationServiceImpl.class);

		// inject the unit's dependencies as mock object via reflection
		ReflectionTestUtils.setField(geoController, "locationService",
				locationService);
	}

	/** Return location entity with service returned parameters */
	private LocationEntity getResponseLocationEntity() {
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
	 * Expected results in model map:
	 * <ul>
	 * <li>Service is invoked properly and returns a LocationEntity object</li>
	 * <li>Service is not allowed to return with an exception.</li>
	 * </ul>
	 */
	@Test
	public void testSetupForm_ValidIpString() {
		// mock return value
		LocationEntity mockResponse_LocationEntity = getResponseLocationEntity();

		Model inputModelMap = new BindingAwareModelMap();

		AppException responseException = null;

		// mock calls
		try {
			expect(locationService.getLocation(VALID_IP4_ADDRESS)).andReturn(
					mockResponse_LocationEntity);
		} catch (AppException ae) {
			responseException = ae;
		}
		replay(locationService);

		// service method call
		geoController.setupForm(VALID_IP4_ADDRESS, LOCALE, inputModelMap);

		verify(locationService);

		Assert.state(inputModelMap.containsAttribute("location"),
				"service must return location attribute in modelmap");
		Assert.state(!inputModelMap.containsAttribute("exception"),
				"service is not allowed to return an exception in modelmap");
		Assert.isNull(responseException,
				"service is not allowed to return an exception right now");

	}

	/**
	 * Test case: call location service with invalid ip.
	 * 
	 * Expected results in model map:
	 * <ul>
	 * <li>Service is invoked properly and returns a Default LocationEntity and
	 * Exception in ModelMap</li>
	 * <li>Mock service must return with an exception.</li>
	 * </ul>
	 */
	@Test
	public void testSetupForm_InValidIpString() {
		// mock return value
		AppException mockResponse_Exception = new AppException();
		mockResponse_Exception.addInfo(ErrorInfoFactory
				.getIllegalClientParameterErrorInfo("GeoControllerTest",
						"invalid Ip address", "provide a valid ip address",
						"INVALID_IP4_ADDRESS", INVALID_IP4_ADDRESS));

		Model inputModelMap = new BindingAwareModelMap();

		AppException responseException = null;

		// mock calls
		try {
			expect(locationService.getLocation(INVALID_IP4_ADDRESS)).andThrow(
					mockResponse_Exception);
		} catch (AppException ae) {
			responseException = ae;
		}
		replay(locationService);

		// service method call
		geoController.setupForm(INVALID_IP4_ADDRESS, LOCALE, inputModelMap);

		verify(locationService);

		Assert.state(inputModelMap.containsAttribute("location"),
				"service must return location attribute in modelmap");
		Assert.state(inputModelMap.containsAttribute("exception"),
				"service must return an exception in modelmap");
		Assert.isNull(responseException,
				"service must return an exception right now");

	}
	
	/**
	 * Test case: call location service with emtpy ip.
	 * 
	 * Expected results in model map:
	 * <ul>
	 * <li>Service is invoked determines client ip</li>
	 * <li>and calls location method with this parameter then returns location for it</li>
	 * <li>Mock service is not allowed to return an exception.</li>
	 * </ul>
	 */
	@Test
	public void testSetupForm_EmptyIpString() {
		// mock return value
		LocationEntity mockResponse_LocationEntity = getResponseLocationEntity();

		Model inputModelMap = new BindingAwareModelMap();
		AppException responseException = null;

		// mock calls
		try {
			expect(locationService.getMyIpAddress()).andReturn(MY_ADDRESS);
			
			expect(locationService.getLocation(MY_ADDRESS)).andReturn(mockResponse_LocationEntity);
		} catch (AppException ae) {
			responseException = ae;
		}
		replay(locationService);

		// service method call
		geoController.setupForm(EMPTY_STRING, LOCALE, inputModelMap);

		verify(locationService);

		Assert.state(inputModelMap.containsAttribute("location"),
				"service must return location attribute in modelmap");
		Assert.state(!inputModelMap.containsAttribute("exception"),
				"service is not allowed to return an exception in modelmap");
		Assert.isNull(responseException,
				"service is not allowed to return an exception right now");

	}
}
