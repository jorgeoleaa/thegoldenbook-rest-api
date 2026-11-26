package com.thegoldenbook.rest.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thegoldenbook.TheGoldenBookException;
import com.thegoldenbook.model.Format;
import com.thegoldenbook.service.FormatService;
import com.thegoldenbook.service.impl.FormatServiceImpl;

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

@Path("/format")
public class FormatResource {

	private FormatService formatService = null;

	private static Logger logger = LogManager.getLogger(FormatResource.class);

	public FormatResource() {
		formatService = new FormatServiceImpl();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "findFormatsByLocale",
			summary = "Search for formats",
			description = "Retrieves a list of formats in the language of the provided locale",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Formats found",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = Format[].class)
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

	public Response findAll(@QueryParam("locale") String locale) {

		List<Format> formats = null;

		try {

			formats = formatService.findAll(locale);

		}catch(TheGoldenBookException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error in the formats search process")
					.build();
		}

		return Response.ok(formats).build();
	}

}
