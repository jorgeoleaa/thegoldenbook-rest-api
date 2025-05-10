package com.thegoldenbook.rest.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.thegoldenbook.service.FileService;
import com.thegoldenbook.service.impl.FileServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/image")
public class FileResource {

	private final FileService fileService;

	public FileResource() {
		fileService = new FileServiceImpl(); 
	}

	@GET
	@Path("/{bookId}/image")
	@Produces({ "image/png", "image/jpeg", MediaType.APPLICATION_OCTET_STREAM })
	@Operation(
			summary = "Retrieve an image of a book",
			description = "Returns an image associated with a book in PNG or JPEG format."
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description = "Image found",
				content = @Content(
						mediaType = "application/octet-stream",
						schema = @Schema(type = "string", format = "binary")
						)
				),
		@ApiResponse(
				responseCode = "404",
				description = "Image not found",
				content = @Content(mediaType = MediaType.TEXT_PLAIN)
				),
		@ApiResponse(
				responseCode = "500",
				description = "Internal server error",
				content = @Content(mediaType = MediaType.TEXT_PLAIN)
				)
	})

	public Response getImageByBookId(
			@PathParam("bookId") Long bookId,
			@QueryParam("locale") String locale
			) {
		try {
			List<File> imageFiles = fileService.getImagesByBookId(locale, bookId);

			if (imageFiles.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity("No images were found for the book with ID: " + bookId)
						.build();
			}

			File imageFile = imageFiles.get(0);
			InputStream fileStream = new FileInputStream(imageFile);
			String mediaType = getMediaType(imageFile.getName());

			return Response.ok(fileStream)
					.type(mediaType)
					.build();

		} catch (IOException e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Error reading the image: " + e.getMessage())
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error retrieving the image: " + e.getMessage())
					.build();
		}
	}


	private String getMediaType(String fileName) {
		if (fileName.endsWith(".png")) {
			return "image/png";
		} else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
			return "image/jpeg";
		}
		return MediaType.APPLICATION_OCTET_STREAM;
	}
}
