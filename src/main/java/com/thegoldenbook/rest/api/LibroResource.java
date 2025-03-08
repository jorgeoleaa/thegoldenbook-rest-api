package com.thegoldenbook.rest.api;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.PinguelaException;
import com.pinguela.thegoldenbook.model.LibroDTO;
import com.pinguela.thegoldenbook.model.Results;
import com.pinguela.thegoldenbook.service.LibroCriteria;
import com.pinguela.thegoldenbook.service.LibroService;
import com.pinguela.thegoldenbook.service.impl.LibroServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/libro")
public class LibroResource {

	private LibroService libroService = null;
	
	private static Logger logger = LogManager.getLogger(LibroResource.class);
	
	public LibroResource() {
		libroService = new LibroServiceImpl();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
		operationId = "findLibrosByCriteria",
	    summary = "Búsqueda de libros por criteria",
	    description = "Recupera una lista de libros en función de los criterios introducidos",
	    responses = {
	        @ApiResponse(
	            responseCode = "200",
	            description = "Libros encontrados",
	            content = @Content(
	                mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = LibroDTO[].class)
	            )
	        ),
	        @ApiResponse(
	            responseCode = "404",
	            description = "No se han encontrado resultados"
	        ),
	        @ApiResponse(
	            responseCode = "400",
	            description = "Error al recuperar los datos"
	        )
	    }
	)
	public Response findByCriteria(
			@QueryParam("id") Long id,
			@QueryParam("nombre") String nombre,
			@QueryParam("isbn") String isbn,
			@QueryParam("desdePrecio") Double desdePrecio,
			@QueryParam("hastaPrecio") Double hastaPrecio,
			@QueryParam("desdeFecha") Date desdeFecha,
			@QueryParam("hastaFecha") Date hastaFecha,
			@QueryParam("generoLiterarioId") Integer generoLiterarioId,
			@QueryParam("clasificacionEdadId") Integer clasificacionEdadId,
			@QueryParam("idiomaId") Integer idiomaId,
			@QueryParam("formatoId") Integer formatoId,
			@QueryParam("locale") String locale){
		
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
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error en el proceso de búsqueda de valoraciones")
					.build();
		}
		
		logger.info(libros.getPage());
		
		return Response.ok(libros.getPage()).build();
	}
	
	
}
