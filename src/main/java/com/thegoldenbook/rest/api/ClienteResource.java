package com.thegoldenbook.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.thegoldenbook.dao.DataException;
import com.pinguela.thegoldenbook.service.ClienteService;
import com.pinguela.thegoldenbook.service.ServiceException;
import com.pinguela.thegoldenbook.service.impl.ClienteServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public class ClienteResource {
	
	private ClienteService clienteService = null;
	
	private static Logger logger = LogManager.getLogger(ClienteResource.class);
	
	public ClienteResource() {
		clienteService = new ClienteServiceImpl();
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
			summary="Eliminación de cliente",
			description="Eliminación de un cliente a partir del id que tiene en base de datos",
			responses= {
					@ApiResponse(
							responseCode = "200",
							description = "Cliente eliminado correctamente"
							),
					@ApiResponse(
							responseCode = "400",
							description = "Error en el proceso de eliminación del cliente"
							)
			}
			)
	public Response delete(@QueryParam("id") Long id) {
		
		if(id == null) {
			return Response.status(Status.BAD_REQUEST).entity("Datos introducidos inválidos").build();
		}
		
		boolean isDeleted = false;
		
		try {
			isDeleted = clienteService.delete(id);
		}catch(ServiceException se) {
			logger.error(se.getMessage(), se);
		} catch (DataException de) {
			logger.error(de.getMessage(), de);
		}
		
		if(isDeleted) {
			return Response.status(Status.OK).entity("Cliente eliminado correctamente").build();
		}else {
			return Response.status(Status.BAD_GATEWAY).entity("Error en el proceso de eliminación del cliente").build();
		}
	}
}
