create schema GEOLOCATION;

set schema GEOLOCATION;

CREATE TABLE iplocation (
 ip_from float NOT NULL,
 ip_to float NOT NULL,
 country_code nvarchar(2) NOT NULL,
 country_name nvarchar(64) NOT NULL,
 region_name nvarchar(128) NOT NULL,
 city_name nvarchar(128) NOT NULL,
 latitude float NOT NULL,
 longitude float NOT NULL,
 zip_code nvarchar(30) NOT NULL,
 time_zone nvarchar(8) NOT NULL
);

insert into iplocation 
SELECT * FROM CSVREAD('d:\Projects\springGeo\IP2LOCATION-LITE-DB11.CSV');