package com.thegoldenbook.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.thegoldenbook.dao.DataException;
import com.pinguela.thegoldenbook.model.ClienteDTO;
import com.pinguela.thegoldenbook.service.ClienteService;
import com.pinguela.thegoldenbook.service.ServiceException;
import com.pinguela.thegoldenbook.service.impl.ClienteServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/cliente")
public class ClienteResource {
	
	private ClienteService clienteService = null;
	
	private static Logger logger = LogManager.getLogger(ClienteResource.class);
	
	public ClienteResource() {
		clienteService = new ClienteServiceImpl();
	}
	
	@DELETE
	@Path("/delete")
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
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			summary="Registro de cliente",
			description="Registro de un cliente introduciendo todos los datos del mismo",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "El cliente ha sido registrado correctamente",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema=@Schema(implementation = ClienteDTO.class)
									)
							),
					@ApiResponse(
							responseCode = "400",
							description = "Error en el proceso de registro del cliente"
							)
			}
			)
	public Response registrar(ClienteDTO cliente) {
		
		try {
			Long id = clienteService.registrar(cliente);
			ClienteDTO newCliente = clienteService.findById(id);
			return Response.status(Status.OK).entity(newCliente).build();
		}catch(Exception pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.BAD_REQUEST).entity("Error en el proceso de registro del cliente").build();
		}
		
		
	}
}
