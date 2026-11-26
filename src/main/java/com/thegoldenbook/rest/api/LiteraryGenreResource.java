package com.thegoldenbook.rest.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thegoldenbook.TheGoldenBookException;
import com.thegoldenbook.model.LiteraryGenre;
import com.thegoldenbook.service.LiteraryGenreService;
import com.thegoldenbook.service.impl.LiteraryGenreServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/genre")
public class LiteraryGenreResource {
	
	private LiteraryGenreService literaryGenreService = null;
	
	private static Logger logger = LogManager.getLogger(LiteraryGenreResource.class);
	
	public LiteraryGenreResource() {
		literaryGenreService = new LiteraryGenreServiceImpl();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
	    operationId = "findLiteraryGenresByLocale",
	    summary = "Search for literary genres",
	    description = "Retrieves a list of literary genres in the language of the provided locale",
	    responses = {
	        @ApiResponse(
	            responseCode = "200",
	            description = "Literary genres found",
	            content = @Content(
	                mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = LiteraryGenre[].class)
	            )
	        ),
	        @ApiResponse(
	            responseCode = "404",
	            description = "No results found"
	        ),
	        @ApiResponse(
	            responseCode = "400",
	            description = "Error retrieving data"
	        )
	    }
	)

	public Response findAll(
			@QueryParam("locale") String locale) {
		
		List<LiteraryGenre> genres = null;
		
		try {
			genres = literaryGenreService.findAll(locale);
		}catch(TheGoldenBookException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error in the literary genre search process")
					.build();
		}
		
		return Response.ok(genres).build();
		
	}
	
	
}
