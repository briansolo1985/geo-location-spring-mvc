package com.epam.training.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.training.dao.LocationDao;
import com.epam.training.dao.url.UrlMyIpDao;
import com.epam.training.domain.LocationEntity;
import com.epam.training.exception.AppException;
import com.epam.training.exception.ErrorInfoFactory;
import com.epam.training.service.LocationService;
import com.epam.training.utils.Ip4Converter;
import com.epam.training.utils.Validator;

/**
 * Service for handling client requests and validations
 * 
 * @author Ferenc Kis
 * @version 1.1
 */
@Service
public class LocationServiceImpl implements LocationService {
	private static final Logger LOG = LoggerFactory
			.getLogger(LocationServiceImpl.class);

	@Autowired
	private LocationDao locationDao;

	@Autowired
	private UrlMyIpDao urlMyIpDao;

	@Autowired
	private Ip4Converter ipConverter;

	@Autowired
	private Validator ipValidator;

	/**
	 * Service entry point for getting LocationEntity Validates and converts ip
	 * address
	 * 
	 * @param String
	 *            ip4 address in canonical format
	 * @return LocationEntity location related to ip address
	 * 
	 * @exception AppException
	 *                on validation error
	 * @see AppException
	 */
	@Override
	public LocationEntity getLocation(String ip4Address) throws AppException {

		if (!ipValidator.validateIp4String(ip4Address)) {
			AppException ae = new AppException();
			ae.addInfo(ErrorInfoFactory
					.getIllegalClientParameterErrorInfo(
							"LocationServiceImpl",
							"invalid ip address",
							"Please provide a valid IP4 address - showing default place",
							ip4Address, ip4Address));
			throw ae;
		}
		LOG.info("{} is a valid IP4 address", ip4Address);

		double convertedIp4Address = ipConverter
				.convertFromStringToDouble(ip4Address);
		LOG.info("double converted ip address is {}", convertedIp4Address);

		return locationDao.find(convertedIp4Address).setIp4Address(ip4Address);
	}

	/**
	 * Service entry point for getting client ip address
	 * 
	 * @return client wan ip address
	 * @exception AppException
	 *                on validation error
	 * @see AppException
	 */
	@Override
	public String getMyIpAddress() throws AppException {
		String myIpAddress = urlMyIpDao.find();
		LOG.info("my wan ip address is {}", myIpAddress);
		return myIpAddress;
	}

}
