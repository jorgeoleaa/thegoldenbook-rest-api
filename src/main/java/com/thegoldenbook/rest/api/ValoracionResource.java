package com.thegoldenbook.rest.api;

import java.util.Date;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.thegoldenbook.dao.DataException;
import com.pinguela.thegoldenbook.model.ValoracionDTO;
import com.pinguela.thegoldenbook.service.ValoracionService;
import com.pinguela.thegoldenbook.service.impl.ValoracionServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/valoracion")
@Singleton
public class ValoracionResource {

	private ValoracionService valoracionService = null;

	private static Logger logger = LogManager.getLogger(ValoracionResource.class);

	public ValoracionResource() {
		valoracionService = new ValoracionServiceImpl();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			summary="Creación de valoración",
			description="Crea una valoracion para un libro asociada a un cliente",
			responses= {
					@ApiResponse(
							responseCode="200",
							description="Valoracion creada correctamente"
							),
					@ApiResponse(
							responseCode="400",
							description="Datos introducidos incorrectos o incompletos"
							),
					@ApiResponse(
							responseCode="500",
							description="Error al crear la valoración"
							)
			}
			)
	public Response create(
			@FormParam("clienteId") Long clienteId, 
			@FormParam("libroId") Long libroId,
			@FormParam("numeroEstrellas") Double numeroEstrellas,
			@FormParam("asunto") String asunto,
			@FormParam("cuerpo") String cuerpo,
			@FormParam("locale") String locale) {

		if (clienteId == null || libroId == null || numeroEstrellas == null || 
				asunto == null || asunto.trim().isEmpty() || 
				cuerpo == null || cuerpo.trim().isEmpty() || 
				locale == null || locale.trim().isEmpty()) {

			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Todos los campos son obligatorios y no pueden estar vacíos")
					.build();
		}

		ValoracionDTO valoracion = new ValoracionDTO();

		valoracion.setClienteId(clienteId);
		valoracion.setLibroId(libroId);
		valoracion.setNumeroEstrellas(numeroEstrellas);
		valoracion.setAsunto(asunto);
		valoracion.setCuerpo(cuerpo);
		valoracion.setFechaPublicacion(new Date());

		try {
			valoracionService.create(valoracion, locale);
			return Response.status(Status.OK).entity("Valoración creada correctamente").build();
		} catch (DataException de) {
			logger.error(de.getMessage(), de);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error en el proceso de creación de la valoración")
					.build();
		}
	}
	
}
