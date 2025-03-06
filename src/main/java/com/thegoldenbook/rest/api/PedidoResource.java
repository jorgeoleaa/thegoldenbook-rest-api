package com.thegoldenbook.rest.api;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.PinguelaException;
import com.pinguela.thegoldenbook.dao.DataException;
import com.pinguela.thegoldenbook.model.Pedido;
import com.pinguela.thegoldenbook.service.MailException;
import com.pinguela.thegoldenbook.service.PedidoCriteria;
import com.pinguela.thegoldenbook.service.PedidoService;
import com.pinguela.thegoldenbook.service.impl.PedidoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/pedido")
public class PedidoResource {

	private PedidoService pedidoService = null; 

	private static Logger logger = LogManager.getLogger(PedidoResource.class	);

	public PedidoResource() {
		pedidoService = new PedidoServiceImpl();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "findPedidosByCriteria",
			summary = "Búsqueda de pedidos por criteria",
			description = "Búsqueda de pedidos a partir de varios parámetros introducidos",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Pedidos encontrados",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = Pedido[].class)
									)
							),
					@ApiResponse(
							responseCode = "400",
							description = "Datos introducidos incorrectos"
							),
					@ApiResponse(
							responseCode = "500",
							description = "Error al procesar la solicitud"
							)
			}
			)
	public Response findByCriteria(
			@QueryParam("id") Long id,
			@QueryParam("fechaDesde") String fechaDesde,
			@QueryParam("fechaHasta") String fechaHasta,
			@QueryParam("precioDesde") Double precioDesde,
			@QueryParam("precioHasta") Double precioHasta,
			@QueryParam("clienteId") Long clienteId,
			@QueryParam("tipoEstadoPedidoId") Integer tipoEstadoPedidoId) {


		PedidoCriteria pedidoCriteria = new PedidoCriteria();
		pedidoCriteria.setId(id);
		pedidoCriteria.setPrecioDesde(precioDesde);
		pedidoCriteria.setPrecioHasta(precioHasta);
		pedidoCriteria.setClienteId(clienteId);
		pedidoCriteria.setTipoEstadoPedidoId(tipoEstadoPedidoId);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (fechaDesde != null) {
				pedidoCriteria.setFechaDesde(formatter.parse(fechaDesde));
			}
			if (fechaHasta != null) {
				pedidoCriteria.setFechaHasta(formatter.parse(fechaHasta));
			}
		} catch (Exception pe) {
			logger.error("Error parseando la fecha: " + pe.getMessage(), pe);
			return Response.status(Status.BAD_REQUEST)
					.entity("Formato de fecha inválido. Usa yyyy-MM-dd.")
					.build();
		}

		try {
			List<Pedido> result = pedidoService.findByCriteria(pedidoCriteria, 1, Integer.MAX_VALUE).getPage();
			return Response.status(Status.OK).entity(result).build();
		} catch (DataException de) {
			logger.error("Data error: " + de.getMessage(), de);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error en el proceso de búsqueda de los pedidos")
					.build();
		}
	}


	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "createPedido",
			summary="Creación de un pedido",
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

	@DELETE
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "deletePedido",
			summary="Eliminación de un pedido",
			description="Elimina un pedido a partir del identificador introducido",
			responses= {
					@ApiResponse(
							responseCode = "200",
							description = "Pedido eliminado correctamente"
							),
					@ApiResponse(
							responseCode="400",
							description="Error en el proceso de eliminación del pedido"
							)
			}
			)
	public Response delete(@QueryParam("id") Long id) {

		try {
			pedidoService.delete(id);
			return Response.status(Status.OK).entity("Pedido eliminado correctamente").build();
		}catch(DataException de) {
			logger.error(de.getMessage(), de);
			return Response.status(Status.BAD_REQUEST).entity("Error en el proceso de eliminación del pedido").build();
		}

	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
	    operationId = "updatePedido",
	    summary = "Actualización de un pedido",
	    description = "Actualiza todos los datos pertenecientes al pedido",
	    responses = {
	        @ApiResponse(
	            responseCode = "200",
	            description = "Pedido actualizado correctamente"
	        ),
	        @ApiResponse(
	            responseCode = "400",
	            description = "No se pudo actualizar el pedido"
	        ),
	        @ApiResponse(
	            responseCode = "500",
	            description = "Error interno en el proceso de actualización del pedido"
	        )
	    }
	)
	public Response update(Pedido pedido) {

		try {
			if(pedidoService.update(pedido)) {
				return Response.ok().entity(pedido).build();
			}else {
				return Response.status(Status.BAD_REQUEST).entity("No se ha podido actualizar el pedido").build();
			}
			
		}catch(PinguelaException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error en el proceso de actualización del pedido").build();
		}
	}
}
