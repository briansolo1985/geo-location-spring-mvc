package com.epam.training.dao;

import com.epam.training.domain.LocationEntity;
import com.epam.training.exception.AppException;

/**
 * DAO interface for LocationEntity
 * 
 * @author Ferenc Kis
 * @version 1.1
 */
public interface LocationDao {
	LocationEntity find(double convertedIp4Address) throws AppException;
}
