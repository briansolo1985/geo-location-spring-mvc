package com.epam.training.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

/**
 * Class for validating ip addresses
 * 
 * @author Ferenc Kis
 * @version 1.1
 */
@Service
public class Validator {

	private static final String IP4_VALIDATOR_REGEXP = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

	/**
	 * Validates ip addresses against IP4 format
	 * 
	 * @param String
	 *            ip address
	 * @return boolean validation result
	 */
	public boolean validateIp4String(String ip4Address) {
		if (ip4Address == null || ip4Address.equals("")) {
			return false;
		}

		Pattern pattern = Pattern.compile(IP4_VALIDATOR_REGEXP);
		Matcher matcher = pattern.matcher(ip4Address);

		return matcher.matches();
	}
}
