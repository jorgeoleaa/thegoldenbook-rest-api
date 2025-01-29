package com.thegoldenbook.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pinguela.thegoldenbook.service.PedidoService;
import com.pinguela.thegoldenbook.service.impl.PedidoServiceImpl;

public class PedidoResource {
	
	private PedidoService pedidoService = null; 
	
	public PedidoResource() {
		pedidoService = new PedidoServiceImpl();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces()
	public Response create() {
		
	}

}
