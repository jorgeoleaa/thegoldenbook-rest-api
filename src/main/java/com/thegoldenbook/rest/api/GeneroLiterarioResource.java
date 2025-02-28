package com.thegoldenbook.rest.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.PinguelaException;
import com.pinguela.thegoldenbook.model.GeneroLiterario;
import com.pinguela.thegoldenbook.service.GeneroLiterarioService;
import com.pinguela.thegoldenbook.service.impl.GeneroLiterarioServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/genero")
public class GeneroLiterarioResource {
	
	private GeneroLiterarioService generoService = null;
	
	private static Logger logger = LogManager.getLogger(GeneroLiterarioResource.class);
	
	public GeneroLiterarioResource() {
		generoService = new GeneroLiterarioServiceImpl();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "findGenerosLiterariosByLocale",
			summary = "Búsqueda de géneros literarios",
			description = "Recupera una lista de géneros literarios en el locale proporcionado",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Generos literarios encontrados",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = GeneroLiterario[].class)
									)
							),
					@ApiResponse(
							responseCode = "404",
							description = "No se han encontrado resultados"
							),
					@ApiResponse(
							responseCode = "400",
							description = "Error al recuperar los datos"
							)
			}
			)
	public Response findAll(
			@QueryParam("locale") String locale) {
		
		List<GeneroLiterario> generos = null;
		
		try {
			generos = generoService.findAll(locale);
		}catch(PinguelaException pe) {
			logger.error(pe.getMessage(), pe);
		}
		
		return Response.ok(generos).build();
		
	}
	
	
}
