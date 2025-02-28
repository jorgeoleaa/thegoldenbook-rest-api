package com.thegoldenbook.rest.api.param;

import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.ParamConverter;

public class DateParamConverter implements ParamConverter<Date> {
	
	@Override
	public Date fromString(String arg0) {
		try {
			return arg0 == null ? null : Date.from(Instant.parse(arg0));
		} catch (Exception e) {
			throw new WebApplicationException(String.format("Cannot parse date %s", arg0));
		}
	}

	@Override
	public String toString(Date arg0) {
		return DateFormat.getInstance().format(arg0);
	}

}
