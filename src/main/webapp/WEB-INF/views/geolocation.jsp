<%@ page contentType="text/html" pageEncoding="UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
	<title><fmt:message key="titleMsg"/></title>
	<link href='<c:url value="/resources/default.css"/>' rel="stylesheet" type="text/css" />
	<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
	<script src='<c:url value="/resources/scripts.js"/>'></script>
</head>
<body>
	<div class="container">
		<div class="header">
			<div class="header-title">
				<fmt:message key="titleMsg"/>
			</div>
			<div class="header-search">
				<form:form modelAttribute="location" method="GET" onsubmit="return validateInput();">
					<fmt:message key="searchMsg"/>
					<form:input path="ip4Address" size="15" maxlength="15" />
					<input class="searchButton" type="submit" value="<fmt:message key="buttonText"/>" />
				</form:form>
			</div>
			<div class="header-date">${date}</div>
		</div>
		<div class="result">
			<div class="result-header"><fmt:message key="resultHead"/></div>
			<div class="result-row-even">
				<div class="result-label"><fmt:message key="countryCode"/></div>
				<div class="result-text">${location.countryCode}</div>
			</div>
			<div class="result-row-odd">
				<div class="result-label"><fmt:message key="country"/></div>
				<div class="result-text">${location.countryName}</div>
			</div>
			<div class="result-row-even">
				<div class="result-label"><fmt:message key="region"/></div>
				<div class="result-text">${location.regionName}</div>
			</div>
			<div class="result-row-odd">
				<div class="result-label"><fmt:message key="city"/></div>
				<div class="result-text">${location.cityName}</div>
			</div>
			<div class="result-row-even">
				<div class="result-label"><fmt:message key="zip"/></div>
				<div class="result-text">${location.zipCode}</div>
			</div>
			<div class="result-row-odd">
				<div class="result-label"><fmt:message key="timezone"/></div>
				<div class="result-text">${location.timeZone}</div>
			</div>
			<div id="exception" class="result-exception">
				<div class="result-exception-title"><fmt:message key="errorTitle"/></div>
				<div id="exception-content" class="result-exception-content"></div>
			</div>
			<script>
				<c:if test="${not empty exception}">
					showErrorWindow("${exception}");
				</c:if>	
			</script>
			<div class="result-languages">
				<div class="result-languages-en" onmousedown="click();" onmouseout="release();">
					<a href='<c:url value="/?ip4Address=${location.ip4Address}&locale=en" />'><fmt:message key="langEng"/></a>
				</div>
				<div class="result-languages-hu">
					<a href="'<c:url value="/?ip4Address=${location.ip4Address}&locale=hu" />'"><fmt:message key="langHun"/></a>	
				</div>
			</div>
		</div>
		<div id="map-gmap" class="gmap"></div>
		<script>
			 map_init(${location.latitude}, ${location.longitude});
		</script>
	</div>
</body>
</html>
