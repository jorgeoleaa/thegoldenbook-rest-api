package com.thegoldenbook.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.thegoldenbook.dao.DataException;
import com.pinguela.thegoldenbook.model.Pedido;
import com.pinguela.thegoldenbook.service.MailException;
import com.pinguela.thegoldenbook.service.PedidoService;
import com.pinguela.thegoldenbook.service.impl.PedidoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/pedido")
public class PedidoResource {
	
	private PedidoService pedidoService = null; 
	
	private static Logger logger = LogManager.getLogger(PedidoResource.class	);
	
	public PedidoResource() {
		pedidoService = new PedidoServiceImpl();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			summary="Creación de pedido",
			description="Crea un pedido introduciendo todos los datos del mismo",
			responses= {
					@ApiResponse(
							responseCode="200",
							description="El pedido fue creado correctamente",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = Pedido.class)
									)
							),
					@ApiResponse(
							responseCode = "400",
							description = "Error en el proceso de creación de pedido"
							),
					@ApiResponse(
							responseCode = "400",
							description = "Error al enviar el correo de creación del pedido"
							)	
			}
			)
	public Response create(Pedido pedido) {
		try {
			Long id = pedidoService.create(pedido);
			Pedido pedidoCreated = pedidoService.findBy(id);
			return Response.status(Status.OK).entity(pedidoCreated).build();
		}catch(DataException de) {
			 logger.error(de.getMessage(), de);
			 return Response.status(Status.BAD_REQUEST).entity("Error en el proceso de creación del pedido").build();
		}catch(MailException me) {
			logger.error("Error al enviar el correo electrónico", me.getMessage(), me);
			return Response.status(Status.BAD_REQUEST).entity("Error al enviar el correo de creación de peiddo").build();
		}
		
	}

}
