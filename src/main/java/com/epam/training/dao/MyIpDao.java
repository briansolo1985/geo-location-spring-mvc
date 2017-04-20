package com.epam.training.dao;

import com.epam.training.exception.AppException;

/**
 * DAO interface for wan ip address
 * 
 * @author Ferenc Kis
 * @version 1.1
 */
public interface MyIpDao {
	String find() throws AppException;
}
