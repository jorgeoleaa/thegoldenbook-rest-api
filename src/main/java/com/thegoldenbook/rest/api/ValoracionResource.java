package com.thegoldenbook.rest.api;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.thegoldenbook.dao.DataException;
import com.pinguela.thegoldenbook.model.ValoracionDTO;
import com.pinguela.thegoldenbook.service.ValoracionService;
import com.pinguela.thegoldenbook.service.impl.ValoracionServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/valoracion")
@Singleton
public class ValoracionResource {

	private ValoracionService valoracionService = null;

	private static Logger logger = LogManager.getLogger(ValoracionResource.class);

	public ValoracionResource() {
		valoracionService = new ValoracionServiceImpl();
	}

@POST
@Path("/{locale}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Operation(
	operationId="createValoracion",
    summary = "Creación de valoración",
    description = "Crea una valoración para un libro asociada a un cliente.",
    responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Valoración creada correctamente."  
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos introducidos incorrectos o incompletos."
        
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error al crear la valoración."
          
        )
    }
)
	public Response create(@PathParam("locale") String locale, ValoracionDTO valoracion){

		try {
			valoracion.setFechaPublicacion(new Date());
			valoracionService.create(valoracion, locale);
			ValoracionDTO valoracionCreated = valoracionService.findByValoracion(valoracion.getClienteId(), valoracion.getLibroId());
			return Response.status(Status.OK).entity(valoracionCreated).build();
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
		operationId="deleteValoracion",
	    summary = "Eliminar una valoración",
	    description = "Elimina una valoración especificando el ID del libro asociado y el ID del cliente que la realizó.",
	    responses = {
	        @ApiResponse(
	            responseCode = "200",
	            description = "La valoración fue eliminada exitosamente."
	        ),
	        @ApiResponse(
	            responseCode = "400",
	            description = "La solicitud no es válida o la valoración no existe."
	        ),
	        @ApiResponse(
	            responseCode = "500",
	            description = "Ocurrió un error interno al intentar eliminar la valoración."
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
