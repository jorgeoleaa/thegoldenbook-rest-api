package com.thegoldenbook.rest.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.PinguelaException;
import com.pinguela.thegoldenbook.model.Idioma;
import com.pinguela.thegoldenbook.service.IdiomaService;
import com.pinguela.thegoldenbook.service.impl.IdiomaServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class IdiomaResource {
	
	private IdiomaService idiomaService = null;
	
	private static Logger logger = LogManager.getLogger(IdiomaResource.class);
	
	public IdiomaResource() {
		idiomaService = new IdiomaServiceImpl();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "findIdiomasByLocale",
			summary = "Búsqueda de idiomas",
			description = "Recupera una lista de idiomas en el idioma del locale proporcionado",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Idiomas encontrados",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = Idioma[].class)
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
		
		List<Idioma> idiomas = null;
		
		try {
			
			idiomas = idiomaService.findAll(locale);
			
		}catch(PinguelaException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error en el proceso de búsqueda de idiomas")
					.build();
		}
		
		return Response.ok(idiomas).build();
		
	}
}
