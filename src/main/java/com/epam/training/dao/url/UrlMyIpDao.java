package com.epam.training.dao.url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Repository;

import com.epam.training.dao.MyIpDao;
import com.epam.training.exception.AppException;
import com.epam.training.exception.ErrorInfoFactory;

/**
 * URL Dao implementation for getting wan ip address
 * 
 * @author Ferenc Kis
 * @version 1.1
 */
@Repository
public class UrlMyIpDao implements MyIpDao {
	private static final String URL = "http://freegeoip.net/csv";
	private static final int HTTP_OK = 200;

	/**
	 * DAO find method for wan ip address
	 * 
	 * @exception AppException
	 *                on internal error
	 * @see AppException
	 */
	public String find() throws AppException {
		return process(retrieve());
	}

	/**
	 * Communication method Retrieves client IP address from remote service
	 * 
	 * @return String retrieved data structure containing client IP address
	 * 
	 * @exception AppException
	 *                on IO error
	 * @see AppException
	 */
	private String retrieve() throws AppException {

		URL url = null;
		HttpURLConnection httpURLConnection = null;
		String inputline = null;
		StringBuilder csvResult = new StringBuilder();

		try {
			url = new URL(URL);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			if (httpURLConnection.getResponseCode() == HTTP_OK) {
				try (BufferedReader br = new BufferedReader(
						new InputStreamReader(
								httpURLConnection.getInputStream()))) {
					while ((inputline = br.readLine()) != null) {
						if (!"".equals(inputline)) {
							csvResult.append(inputline);
						}
					}
				}
			} else {
				throw new IOException(
						String.format(
								"url connection error: response code=%d response message=%s",
								httpURLConnection.getResponseCode(),
								httpURLConnection.getResponseMessage()));
			}
		} catch (IOException ioe) {
			AppException ae = new AppException();
			ae.addInfo(ErrorInfoFactory.getUrlDaoErrorInfo("UrlMyIpDao",
					"could not access remote service - maybe not operational",
					ioe));
			throw ae;
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}

		return csvResult.toString();
	}

	/**
	 * Communication method Retrieves client IP address from remote service
	 * 
	 * @param String
	 *            string containing client wan properties in csv format
	 * 
	 * @return String client IP address
	 * 
	 * @exception AppException
	 *                on parse error
	 * @see AppException
	 */
	private String process(String csvString) throws AppException {

		String[] splitArray = null;
		String myIpAddress = "";

		if (csvString != null) {
			splitArray = csvString.split(",");
			if (splitArray.length > 0) {
				myIpAddress = splitArray[0].replace("\"", "");
			}
		}

		if (myIpAddress.equals("")) {
			AppException ae = new AppException();
			ae.addInfo(ErrorInfoFactory.getUrlDaoErrorInfo("UrlMyIpDao",
					"remote service returned corrupted file", null)
					.setParameter("csvString", csvString));
			throw ae;
		}

		return myIpAddress;
	}
}
