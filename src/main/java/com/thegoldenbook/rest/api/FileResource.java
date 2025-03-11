package com.thegoldenbook.rest.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.pinguela.thegoldenbook.service.FileService;
import com.pinguela.thegoldenbook.service.impl.FileServiceImpl;

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
	@Path("/{libroId}/image") 
	@Produces({ "image/png", "image/jpeg", MediaType.APPLICATION_OCTET_STREAM })
	@Operation(
	    summary = "Obtener una imagen de un libro",
	    description = "Devuelve una imagen asociada a un libro en formato PNG o JPEG."
	)
	@ApiResponses({
	    @ApiResponse(
	        responseCode = "200",
	        description = "Imagen encontrada",
	        content = @Content(
	            mediaType = "application/octet-stream",
	            schema = @Schema(type = "string", format = "binary")
	        )
	    ),
	    @ApiResponse(
	        responseCode = "404",
	        description = "Imagen no encontrada",
	        content = @Content(mediaType = MediaType.TEXT_PLAIN)
	    ),
	    @ApiResponse(
	        responseCode = "500",
	        description = "Error interno del servidor",
	        content = @Content(mediaType = MediaType.TEXT_PLAIN)
	    )
	})
	public Response getImageByBookId(
	    @PathParam("libroId") Long libroId,
	    @QueryParam("locale") String locale
	) {
	    try {
	        List<File> imageFiles = fileService.getImagesByBookId(locale, libroId);

	        if (imageFiles.isEmpty()) {
	            return Response.status(Response.Status.NOT_FOUND)
	                    .entity("No se encontraron imágenes para el libro con ID: " + libroId)
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
	                .entity("Error al leer la imagen: " + e.getMessage())
	                .build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Error al obtener la imagen: " + e.getMessage())
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
