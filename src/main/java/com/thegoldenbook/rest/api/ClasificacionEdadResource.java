package com.thegoldenbook.rest.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.PinguelaException;
import com.pinguela.thegoldenbook.model.ClasificacionEdad;
import com.pinguela.thegoldenbook.service.ClasificacionEdadService;
import com.pinguela.thegoldenbook.service.impl.ClasificacionEdadServiceImpl;

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
import jakarta.ws.rs.core.Response.Status;

@Path("/edad")
public class ClasificacionEdadResource {

	
	private ClasificacionEdadService edadService = null;
	
	private static Logger logger = LogManager.getLogger(ClasificacionEdadResource.class);
	
	public ClasificacionEdadResource() {
		edadService = new ClasificacionEdadServiceImpl();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "findEdadesByLocale",
			summary = "Búsqueda de clasficaciones por edad de los libros",
			description = "Recupera una lista de clasificaciones por edad en el idioma del locale proporcionado",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Clasificaciones por edad encontradas",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = ClasificacionEdad[].class)
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
		
		List<ClasificacionEdad> edades = null;
		
		try {
			
			edades = edadService.findAll(locale);
			
		}catch(PinguelaException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error en el proceso de búsqueda de clasificaciones por edad")
					.build();
		}
		
		return Response.ok(edades).build();
	}
}
