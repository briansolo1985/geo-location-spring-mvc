package com.epam.training.utils;

import org.springframework.stereotype.Service;

/**
 * Class for converting string ip4 addresses to double format (DB11 database)
 * 
 * @author Ferenc Kis
 * @version 1.1
 */
@Service
public class Ip4Converter {

	/**
	 * Converts ip address to DB11 format
	 * 
	 * @param String
	 *            ip4Address to convert
	 * @return double converted ip address
	 */
	public double convertFromStringToDouble(String ip4Address) {
		double num = 0;
		if (ip4Address == null || ip4Address.equals("")) {
			num = 0;
		} else {
			String[] arrDec = ip4Address.split("\\.");
			try {
				for (int i = arrDec.length - 1; i >= 0; i--) {
					num += (Integer.parseInt(arrDec[i]) % 256)
							* Math.pow(256, (3 - i));
				}
			} catch (NumberFormatException nfe) {
				num = 0;
			}
		}
		return num;

	}
}
