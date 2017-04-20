package com.epam.training.service;

import org.springframework.transaction.annotation.Transactional;

import com.epam.training.domain.LocationEntity;
import com.epam.training.exception.AppException;

/**
 * Interface for LocationService implementations
 * 
 * @author Ferenc Kis
 * @version 1.1
 */
@Transactional(readOnly=true)
public interface LocationService {
	
	LocationEntity getLocation(String ip4Address) throws AppException;
	
	String getMyIpAddress() throws AppException;
	
}
