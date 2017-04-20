package com.epam.training.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.epam.training.domain.LocationEntity;
import com.epam.training.exception.AppException;
import com.epam.training.exception.ErrorInfoFactory;
import com.epam.training.service.LocationService;

/**
 * Controller class - entry point for web clients Serving / requests
 * 
 * @author Ferenc Kis
 * @version 1.1
 */
@Controller
@RequestMapping("/")
public class GeoController {

	private static final Logger LOG = LoggerFactory
			.getLogger(GeoController.class);

	@Autowired
	private LocationService locationService;

	/**
	 * Method for processing GET requests Exactly for calculating ip address
	 * location
	 * 
	 * @param String
	 *            request paramter ip4Address
	 * @param Locale
	 *            current locale
	 * @param Model
	 *            model for jsp page
	 * @see AppException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(
			@RequestParam(value = "ip4Address", required = false) String address,
			Locale locale, Model model) {

		LocationEntity locationEntity = null;
		try {
			// getting actual date for top right corner
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG,
					locale);
			String todaysDate = dateFormat.format(new Date());
			model.addAttribute("date", todaysDate);
			LOG.info("Today's date is {}", todaysDate);

			// low level check - if parameter is missing,
			// local client wan ip will be used
			String ipAddress = null;
			ipAddress = (StringUtils.hasText(address)) ? address
					: locationService.getMyIpAddress();
			LOG.info("Getting location for IP {}", ipAddress);

			// getting ip location
			locationEntity = locationService.getLocation(ipAddress);
			if (locationEntity.isUnknownLocation()) {
				AppException ae = new AppException();
				ae.addInfo(ErrorInfoFactory
						.getIllegalReturnErrorInfo(
								"GeoController",
								"ip address was not found in database - maybe local or sensitive",
								"IP address was not found - maybe local or sensitive",
								"ipAddress", ipAddress));
				throw ae;
			}
		} catch (AppException ae) {
			model.addAttribute(
					"exception",
					"".equals(ae.getUserLog()) ? "Ooops... Something went wrong, please check error log!"
							: ae.getUserLog());
			LOG.error("{}", ae);
		} catch (Throwable t) {
			model.addAttribute("exception",
					"Ooops... Something went wrong, please check error log!");
			LOG.error("{}", t);
		} finally {
			if (locationEntity == null) {
				locationEntity = new LocationEntity();
			}
			model.addAttribute("location", locationEntity);
			LOG.info("Found location is {}", locationEntity);
		}

		return "geolocation";
	}
}
