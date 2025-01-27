package com.thegoldenbook.rest.api;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.PinguelaException;
import com.pinguela.thegoldenbook.model.LibroDTO;
import com.pinguela.thegoldenbook.model.Results;
import com.pinguela.thegoldenbook.service.LibroCriteria;
import com.pinguela.thegoldenbook.service.LibroService;
import com.pinguela.thegoldenbook.service.impl.LibroServiceImpl;

@Path("libro")
public class LibroResource {

	private LibroService libroService = null;
	
	private static Logger logger = LogManager.getLogger(LibroResource.class);
	
	public LibroResource() {
		libroService = new LibroServiceImpl();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response findByCriteria(
			@FormParam("id") Long id,
			@FormParam("nombre") String nombre,
			@FormParam("isbn") String isbn,
			@FormParam("desdePrecio") Double desdePrecio,
			@FormParam("hastaPrecio") Double hastaPrecio,
			@FormParam("desdeFecha") Date desdeFecha,
			@FormParam("hastaFecha") Date hastaFecha,
			@FormParam("generoLiterarioId") Integer generoLiterarioId,
			@FormParam("clasificacionEdadId") Integer clasificacionEdadId,
			@FormParam("idiomaId") Integer idiomaId,
			@FormParam("formatoId") Integer formatoId,
			@FormParam("locale") String locale){
		
		LibroCriteria criteria = new LibroCriteria();
		
		criteria.setId(id);
		criteria.setNombre(nombre);
		criteria.setIsbn(isbn);
		criteria.setDesdePrecio(desdePrecio);
		criteria.setHastaPrecio(hastaPrecio);
		criteria.setDesdeFecha(desdeFecha);
		criteria.setHastaFecha(hastaFecha);
		criteria.setGeneroLiterarioId(generoLiterarioId);
		criteria.setClasificacionEdadId(clasificacionEdadId);
		criteria.setIdiomaId(idiomaId);
		criteria.setFormatoId(formatoId);
		criteria.setLocale(locale);
		
		Results<LibroDTO> libros = null;
		
		try {
			
			libros = libroService.findByCriteria(criteria, 1, Integer.MAX_VALUE);
			
		}catch(PinguelaException pe) {
			logger.error(pe.getMessage(), pe);
		}
		
		logger.info(libros.getPage());
		
		return Response.ok(libros.getPage()).build();
	}
	
	
}
