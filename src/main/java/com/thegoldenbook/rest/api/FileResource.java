package com.thegoldenbook.rest.api;

import java.io.File;
import java.util.List;

import com.pinguela.thegoldenbook.service.FileService;
import com.pinguela.thegoldenbook.service.impl.FileServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
	
	private FileService fileService = null;
	
	public FileResource() {
		fileService = new FileServiceImpl();
	}
	
	 @GET
	    @Path("/{libroId}/imagenes")
	    @Produces(MediaType.APPLICATION_JSON)
	    @Operation(
	            summary = "Obtener imágenes de un libro",
	            description = "Devuelve una lista de rutas de imágenes asociadas a un libro específico."
	    )
	    @ApiResponses({
	            @ApiResponse(
	                    responseCode = "200",
	                    description = "Imágenes encontradas",
	                    content = @Content(
	                            mediaType = MediaType.APPLICATION_JSON
	                    )
	            ),
	            @ApiResponse(
	                    responseCode = "500",
	                    description = "Error interno del servidor",
	                    content = @Content(
	                            mediaType = MediaType.TEXT_PLAIN
	                    )
	            )
	    })
	    public Response getImagesByBookId(
	            @PathParam("libroId") Long libroId,
	            @QueryParam("locale") String locale 
	    ) {
	        try {
	            List<File> imageFiles = fileService.getImagesByBookId(locale, libroId);

	            List<String> imagePaths = imageFiles.stream()
	                    .map(File::getAbsolutePath)
	                    .toList();

	            return Response.ok(imagePaths).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Error al obtener las imágenes: " + e.getMessage())
	                    .build();
	        }
	    }
}
