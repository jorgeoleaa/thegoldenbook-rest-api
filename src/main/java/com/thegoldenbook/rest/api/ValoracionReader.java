package com.thegoldenbook.rest.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.pinguela.thegoldenbook.model.Pedido;
import com.pinguela.thegoldenbook.model.ValoracionDTO;

@Provider
public class ValoracionReader implements MessageBodyReader<ValoracionDTO>{

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Pedido.class.isAssignableFrom(type);
	}

	@Override
	public ValoracionDTO readFrom(Class<ValoracionDTO> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		
		Gson gson = new Gson();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(entityStream, StandardCharsets.UTF_8));
		
		ValoracionDTO valoracion = gson.fromJson(reader, ValoracionDTO.class);
		
		return valoracion;
		
	}

}
