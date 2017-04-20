package com.epam.training.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.epam.training.dao.LocationDao;
import com.epam.training.domain.LocationEntity;
import com.epam.training.exception.AppException;
import com.epam.training.exception.ErrorInfoFactory;

/**
 * JDBC Dao implementation for LocalitonEntity classes
 * 
 * @author Ferenc Kis
 * @version 1.1
 */
@Repository
public class SimpleJdbcLocationEntityImpl implements LocationDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * DAO find method for single LocationEntity
	 * 
	 * @param double converted ip4Address
	 * @exception AppException
	 *                on data access error, database server failure, etc...
	 * @see AppException
	 */
	public LocationEntity find(double convertedIp4Address) throws AppException {
		LocationEntity locationEntity = null;

		try {
			locationEntity = jdbcTemplate
					.queryForObject(
							"SELECT country_code, country_name, region_name, city_name, "
									+ "latitude, longitude, zip_code, time_zone "
									+ "FROM GEOLOCATION.IPLOCATION WHERE ? BETWEEN ip_from AND ip_to",
							new UserEntityRowMapper(), convertedIp4Address);
		} catch (DataAccessException dae) {
			AppException ae = new AppException();
			ae.addInfo(ErrorInfoFactory.getJdbcDaoErrorInfo(
					"SimpleJdbcLocationEntityImpl",
					"could not access database - maybe not operational", dae));
			throw ae;
		}

		return locationEntity;
	}

	/**
	 * RowMapper for jdbcTemplate Maps location entity database records to
	 * LocationEntity objects
	 * 
	 */
	private class UserEntityRowMapper implements RowMapper<LocationEntity> {

		public LocationEntity mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			LocationEntity locationEntity = new LocationEntity();

			locationEntity.setCountryCode(rs.getString("country_code"));
			locationEntity.setCountryName(rs.getString("country_name"));
			locationEntity.setRegionName(rs.getString("region_name"));
			locationEntity.setCityName(rs.getString("city_name"));
			locationEntity.setLatitude(rs.getDouble("latitude"));
			locationEntity.setLongitude(rs.getDouble("longitude"));
			locationEntity.setZipCode(rs.getString("zip_code"));
			locationEntity.setTimeZone(rs.getString("time_zone"));
			locationEntity.setDefaultAddress(false);

			return locationEntity;
		}
	}
}
