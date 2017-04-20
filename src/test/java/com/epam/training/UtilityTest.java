package com.epam.training;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.epam.training.utils.Ip4Converter;
import com.epam.training.utils.Validator;

public class UtilityTest {

	private static final String INVALID_IP4_ADDRESS = "256.256.0.3";
	private static final String VALID_IP4_ADDRESS = "192.168.100.199";
	private static final String BAD_IP4_ADDRESS = "25a.f56.0.3";

	private Ip4Converter converter;

	private Validator validator;

	@Before
	public void setup() {
		converter = new Ip4Converter();
		validator = new Validator();
	}

	/** Test case: validate a valid ip string
     * 
     * Expected result: 
     * validation returns true
     */
	@Test
	public void testValidIp4AddressValidation() {
		Assert.assertTrue("test failed on a valid IP4 address: "
				+ VALID_IP4_ADDRESS,
				validator.validateIp4String(VALID_IP4_ADDRESS));
	}

	/** Test case: validate an invalid (out of range) ip string
     * 
     * Expected result: 
     * validation returns false
     */
	@Test
	public void testInvalidIp4AddressValidation() {
		Assert.assertFalse("test not failed on an invalid IP4 address: "
				+ INVALID_IP4_ADDRESS,
				validator.validateIp4String(INVALID_IP4_ADDRESS));
	}

	/** Test case: validate a bad (not an ip) ip string
     * 
     * Expected result: 
     * validation returns false
     */
	@Test
	public void testBadIp4AddressValidation() {
		Assert.assertFalse("test not failed on a bad IP4 address: "
				+ BAD_IP4_ADDRESS, validator.validateIp4String(BAD_IP4_ADDRESS));
	}

	/** Test case: validate a null string
     * 
     * Expected result: 
     * validation returns false
     */
	@Test
	public void testNullStringIp4AddressValidation() {
		Assert.assertFalse("test not failed on a null string",
				validator.validateIp4String(null));
	}

	/** Test case: validate an empty ip string
     * 
     * Expected result: 
     * validation returns false
     */
	@Test
	public void testEmptyStringIp4AddressValidation() {
		Assert.assertFalse("test not failed on an empty string",
				validator.validateIp4String(""));
	}

	/** Test case: convert a null string
     * 
     * Expected result: 
     * converter returns 0.0
     */
	@Test
	public void testNullStringIp4AddressConverter() {
		Assert.assertTrue("test not failed on a null string",
				converter.convertFromStringToDouble(null) == 0.0);
	}

	/** Test case: convert an empty string
     * 
     * Expected result: 
     * converter returns 0.0
     */
	@Test
	public void testEmptyStringIp4AddressConverter() {
		Assert.assertTrue("test not failed on an empty string",
				converter.convertFromStringToDouble("") == 0.0);
	}

	/** Test case: convert a valid ip string
     * 
     * Expected result: 
     * converter returns a double greater than 0.0
     */
	@Test
	public void testValidIp4AddressConverter() {
		Assert.assertTrue("test failed on a convertable IP4 address",
				converter.convertFromStringToDouble(VALID_IP4_ADDRESS) > 0.0);
	}

	/** Test case: convert an invalid ip string
     * 
     * Expected result: 
     * converter returns a double greater than 0.0
     */
	@Test
	public void testInvalidIp4AddressConverter() {
		Assert.assertTrue("test failed on a convertable IP4 address",
				converter.convertFromStringToDouble(INVALID_IP4_ADDRESS) > 0.0);
	}

	/** Test case: convert a bad ip string
     * 
     * Expected result: 
     * converter returns a double greater than 0.0
     */
	@Test
	public void testBadIp4AddressConverter() {
		Assert.assertTrue("test not failed on a inconvertable IP4 address",
				converter.convertFromStringToDouble(BAD_IP4_ADDRESS) == 0.0);
	}
}
