package com.thegoldenbook.rest.api.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DateParamConverterProvider implements ParamConverterProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <T> ParamConverter<T> getConverter(Class<T> arg0, Type arg1, Annotation[] arg2) {
		return Date.class.isAssignableFrom(arg0) ? (ParamConverter<T>) new DateParamConverter() : null;
	}

}
