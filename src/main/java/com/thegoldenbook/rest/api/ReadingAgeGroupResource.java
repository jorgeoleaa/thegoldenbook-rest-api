package com.thegoldenbook.rest.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thegoldenbook.TheGoldenBookException;
import com.thegoldenbook.model.ReadingAgeGroup;
import com.thegoldenbook.service.ReadingAgeGroupService;
import com.thegoldenbook.service.impl.ReadingAgeGroupServiceImpl;

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

@Path("/age")
public class ReadingAgeGroupResource {

	
	private ReadingAgeGroupService readingAgeGroupService = null;
	
	private static Logger logger = LogManager.getLogger(ReadingAgeGroupResource.class);
	
	public ReadingAgeGroupResource() {
		readingAgeGroupService = new ReadingAgeGroupServiceImpl();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
	    operationId = "findEdadesByLocale",
	    summary = "Search for book reading age groups",
	    description = "Retrieves a list of reading age groups in the language of the provided locale",
	    responses = {
	        @ApiResponse(
	            responseCode = "200",
	            description = "Reading age groups found",
	            content = @Content(
	                mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = ReadingAgeGroup[].class)
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
		
		List<ReadingAgeGroup> readingAgeGroups = null;
		
		try {
			
			readingAgeGroups = readingAgeGroupService.findAll(locale);
			
		}catch(TheGoldenBookException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error in the reading age groups search process")
					.build();
		}
		
		return Response.ok(readingAgeGroups).build();
	}
}
