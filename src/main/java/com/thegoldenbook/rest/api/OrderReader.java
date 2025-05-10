package com.thegoldenbook.rest.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.thegoldenbook.model.Order;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;

@Provider
public class OrderReader implements MessageBodyReader<Order> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Order.class.isAssignableFrom(type);
	}

	@Override
	public Order readFrom(Class<Order> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		
		Gson gson = new Gson();
		BufferedReader reader = new BufferedReader(new InputStreamReader(entityStream, StandardCharsets.UTF_8));
		
		Order order = gson.fromJson(reader, Order.class);
		
		return order;
	}

}
