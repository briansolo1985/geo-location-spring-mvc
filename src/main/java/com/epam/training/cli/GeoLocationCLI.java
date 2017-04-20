package com.epam.training.cli;

import java.io.Console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.epam.training.domain.LocationEntity;
import com.epam.training.exception.AppException;
import com.epam.training.exception.ErrorInfoFactory;
import com.epam.training.service.LocationService;

/**
 * Command line interface for GeoLocation App *
 * 
 * @author Ferenc Kis
 * @version 1.1
 */
public class GeoLocationCLI {

	private static final Logger LOG = LoggerFactory
			.getLogger(GeoLocationCLI.class);

	private static final String JDBC_CONTEXT = "classpath:context-jdbc.xml";
	private static final String SERVICE_CONTEXT = "classpath:context-service.xml";

	@Autowired
	private LocationService locationService;

	private String ip4Address;

	public GeoLocationCLI(String ip4Address) throws AppException {
		this.ip4Address = ip4Address;

		loadContext();
	}

	/**
	 * Loads application context
	 * 
	 * @exception AppException
	 *                on context initialization error.
	 * @see AppException
	 */
	private void loadContext() throws AppException {
		LOG.info("Loading context...");

		ApplicationContext context = new ClassPathXmlApplicationContext(
				JDBC_CONTEXT, SERVICE_CONTEXT);
		AutowireCapableBeanFactory acbFactory = context
				.getAutowireCapableBeanFactory();
		try {
			acbFactory.autowireBean(this);
		} catch (BeansException e) {
			AppException ae = new AppException();
			ae.addInfo(ErrorInfoFactory.getInitializaionErrorInfo(
					"GeoLocationCLI", "could not initialize context",
					"context", context));
			throw ae;
		}

	}

	/**
	 * Gets location for member variable ip4Address (can be populated through
	 * constructor)
	 * 
	 * @exception AppException
	 *                on internal errors.
	 * @see AppException
	 */
	public LocationEntity getLocation() throws AppException {

		LOG.info("Querying location for {}", ip4Address);
		return locationService.getLocation(ip4Address);

	}

	/**
	 * Application entry point from command line
	 * 
	 */
	public static void main(String[] args) {

		Console console;
		String inputString;

		try {
			console = System.console();

			// checks for valid console
			if (console == null) {
				AppException ae = new AppException();
				ae.addInfo(ErrorInfoFactory.getInitializaionErrorInfo(
						"GeoLocationCLI", "could not initialize console",
						"console", console));
				throw ae;
			}

			// basic input check - not null or empty string
			inputString = console
					.readLine("Please enter a valid IP4 address: ");

			if (inputString == null || inputString.equals("")) {
				AppException ae = new AppException();
				ae.addInfo(ErrorInfoFactory.getIllegalClientParameterErrorInfo(
						"GeoLocationCLI", "input parameter is null",
						"Please provide a valid IP address", "inputString",
						inputString));
				throw ae;
			}

			// calling service
			GeoLocationCLI geoLocationCLI = new GeoLocationCLI(inputString);

			LOG.info("Found location is {}", geoLocationCLI.getLocation());
		} catch (AppException ae) {
			LOG.info(
					"{}",
					"".equals(ae.getUserLog()) ? "Ooops... Something went wrong, please check error log!"
							: ae.getUserLog());
			LOG.error("{}", ae);
		} catch (Throwable t) {
			LOG.info("{}",
					"Ooops... Something went wrong, please check error log!");
			LOG.error("{}", t);
		}
	}
}
