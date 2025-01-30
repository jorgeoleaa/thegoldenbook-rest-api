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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pinguela.thegoldenbook.dao.DataException;
import com.pinguela.thegoldenbook.model.Pedido;
import com.pinguela.thegoldenbook.service.MailException;
import com.pinguela.thegoldenbook.service.PedidoService;
import com.pinguela.thegoldenbook.service.impl.PedidoServiceImpl;

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
	public Response create(Pedido pedido) {
		try {
			Long id = pedidoService.create(pedido);
			return Response.status(Status.OK).entity(id).build();
		}catch(DataException de) {
			 logger.error(de.getMessage(), de);
			 return Response.status(Status.BAD_REQUEST).entity("Error en el proceso de creación del pedido").build();
		}catch(MailException me) {
			logger.error("Error al enviar el correo electrónico", me.getMessage(), me);
			return Response.status(Status.BAD_REQUEST).entity("Error al enviar el correo de creación de peiddo").build();
		}
		
	}

}
