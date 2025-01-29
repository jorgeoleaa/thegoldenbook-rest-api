package com.thegoldenbook.rest.api;

import java.util.Date;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    summary = "Creación de valoración",
    description = "Crea una valoración para un libro asociada a un cliente.",
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Valoración creada correctamente.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos introducidos incorrectos o incompletos.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error al crear la valoración.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
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

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
	    summary = "Eliminar una valoración",
	    description = "Elimina una valoración especificando el ID del libro asociado y el ID del cliente que la realizó.",
	    responses = {
	        @ApiResponse(
	            responseCode = "200",
	            description = "La valoración fue eliminada exitosamente.",
	            content = @Content(
	            		mediaType = MediaType.APPLICATION_JSON, 
	            		schema = @Schema(implementation = String.class)
	            		)
	        ),
	        @ApiResponse(
	            responseCode = "400",
	            description = "La solicitud no es válida o la valoración no existe.",
	            content = @Content(
	            		mediaType = MediaType.APPLICATION_JSON, 
	            		schema = @Schema(implementation = String.class)
	            		)
	        ),
	        @ApiResponse(
	            responseCode = "500",
	            description = "Ocurrió un error interno al intentar eliminar la valoración.",
	            content = @Content(
	            		mediaType = MediaType.APPLICATION_JSON, 
	            		schema = @Schema(implementation = String.class)
	            		)
	        )
	    }
	)
	public Response delete(
	    @QueryParam("libroId") Long libroId,
	    @QueryParam("clienteId") Long clienteId
	) {
	    if (libroId == null || clienteId == null) {
	        return Response.status(Status.BAD_REQUEST)
	            .entity("Todos los campos son obligatorios y no pueden estar vacíos.")
	            .build();
	    }

	    try {
	        boolean deleted = valoracionService.delete(clienteId, libroId);
	        if (deleted) {
	            return Response.status(Status.OK)
	                .entity("Valoración eliminada correctamente.")
	                .build();
	        } else {
	            return Response.status(Status.BAD_REQUEST)
	                .entity("No se encontró la valoración para eliminar.")
	                .build();
	        }
	    } catch (DataException e) {
	        logger.error("Error al eliminar la valoración: {}", e.getMessage(), e);
	        return Response.status(Status.INTERNAL_SERVER_ERROR)
	            .entity("Ocurrió un error al procesar la solicitud.")
	            .build();
	    }
	}

}
