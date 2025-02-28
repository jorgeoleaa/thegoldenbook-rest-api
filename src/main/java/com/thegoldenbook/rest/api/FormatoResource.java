package com.thegoldenbook.rest.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.PinguelaException;
import com.pinguela.thegoldenbook.model.Formato;
import com.pinguela.thegoldenbook.service.FormatoService;
import com.pinguela.thegoldenbook.service.impl.FormatoServiceImpl;

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

@Path("/formato")
public class FormatoResource {
	
	private FormatoService formatoService = null;
	
	private static Logger logger = LogManager.getLogger(FormatoResource.class);
	
	public FormatoResource() {
		formatoService = new FormatoServiceImpl();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "findFormatosByLocale",
			summary = "Búsqueda de formatos",
			description = "Recupera una lista de formatos en el idioma del locale proporcionado",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Formatos encontrados",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = Formato[].class)
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
	public Response findAll(@QueryParam("locale") String locale) {
			
		List<Formato> formatos = null;
		
		try {
			
			formatos = formatoService.findAll(locale);
			
		}catch(PinguelaException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error en el proceso de búsqueda de los formatos")
					.build();
		}
		
		return Response.ok(formatos).build();
	}

}
