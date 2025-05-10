package com.thegoldenbook.rest.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.thegoldenbook.model.User;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;

public class UserReader implements MessageBodyReader<User>{

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return User.class.isAssignableFrom(type);
	}

	@Override
	public User readFrom(Class<User> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		
		Gson gson = new Gson();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(entityStream, StandardCharsets.UTF_8));
	
		User user = gson.fromJson(reader, User.class);
		
		return user;
	}

}
