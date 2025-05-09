package com.thegoldenbook.rest.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thegoldenbook.TheGoldenBookException;
import com.thegoldenbook.model.Language;
import com.thegoldenbook.service.LanguageService;
import com.thegoldenbook.service.impl.LanguageServiceImpl;

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

@Path("/language")
public class LanguageResource {
	
	private LanguageService languageService = null;
	
	private static Logger logger = LogManager.getLogger(LanguageResource.class);
	
	public LanguageResource() {
		languageService = new LanguageServiceImpl();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
	    operationId = "findLanguagesByLocale",
	    summary = "Language search",
	    description = "Retrieves a list of languages in the language of the provided locale",
	    responses = {
	        @ApiResponse(
	            responseCode = "200",
	            description = "Languages found",
	            content = @Content(
	                mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Language[].class)
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
		
		List<Language> languages = null;
		
		try {
			
			languages = languageService.findAll(locale);
			
		}catch(TheGoldenBookException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error in the language search process")
					.build();
		}
		
		return Response.ok(languages).build();
		
	}
}
