package com.thegoldenbook.rest.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thegoldenbook.TheGoldenBookException;
import com.thegoldenbook.dao.DataException;
import com.thegoldenbook.model.User;
import com.thegoldenbook.rest.api.dto.UserCredentials;
import com.thegoldenbook.service.ServiceException;
import com.thegoldenbook.service.UserService;
import com.thegoldenbook.service.impl.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/user")
public class UserResource {

	private UserService userService = null;

	private static Logger logger = LogManager.getLogger(UserResource.class);

	public UserResource() {
		userService = new UserServiceImpl();
	}

	@DELETE
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "deleteUser",
			summary="User deletion",
			description="Deletes a user based on the ID they have in the database",
			responses= {
					@ApiResponse(
							responseCode = "200",
							description = "User deleted successfully"
							),
					@ApiResponse(
							responseCode = "400",
							description = "Error in the user deletion process"
							)
			}
			)

	public Response delete(@QueryParam("id") Long id) {

		if(id == null) {
			return Response.status(Status.BAD_REQUEST).entity("Invalid data entered").build();
		}

		boolean isDeleted = false;

		try {
			isDeleted = userService.delete(id);
		}catch(ServiceException se) {
			logger.error(se.getMessage(), se);
		} catch (DataException de) {
			logger.error(de.getMessage(), de);
		}

		if(isDeleted) {
			return Response.status(Status.OK).entity("User successfully deleted").build();
		}else {
			return Response.status(Status.BAD_GATEWAY).entity("Error in the user deletion process").build();
		}
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId="registerUser",
			summary="User registration",
			description="Registers a user by entering all their details",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "The user has been registered successfully",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema=@Schema(implementation = User.class)
									)
							),
					@ApiResponse(
							responseCode = "400",
							description = "Error in the user registration process"
							)
			}
			)
	public Response registrar(User user) {

		try {
			Long id = userService.register(user);
			User newCliente = userService.findById(id);
			return Response.status(Status.OK).entity(newCliente).build();
		}catch(Exception pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.BAD_REQUEST).entity("Error in the user registration process").build();
		}


	}

	@POST
	@Path("/auth")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "authenticateUser",
			summary = "User authentication",
			description = "Authenticates a user by entering their email and password",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Authentication process successful",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = User.class)
									)
							),
					@ApiResponse(
							responseCode = "400",
							description = "Error in the authentication process"
							)
			}
			)
	public Response autenticar(UserCredentials credentials) {

		User authenticatedUser = null;

		try {

			authenticatedUser = userService.authenticate(credentials.getEmail(), credentials.getPassword());

		}catch(TheGoldenBookException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.BAD_REQUEST).entity("Error in the user authentication process").build();
		}

		return Response.status(Status.OK).entity(authenticatedUser).build();
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "updateUser",
			summary = "Update a user",
			description = "Updates a user by entering all their data",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "The user was successfully updated",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = User.class)
									)
							),
					@ApiResponse(
							responseCode = "400",
							description = "Incorrect or incomplete data entered"
							),
					@ApiResponse(
							responseCode = "500",
							description = "Error in the user update process"
							)
			}
			)
	public Response update (User cliente) {

		try {

			boolean isUpdated = userService.update(cliente);
			if(isUpdated) {
				User updatedUser = userService.findById(cliente.getId());
				return Response.status(Status.OK).entity(updatedUser).build();
			}else {
				return Response.status(Status.BAD_REQUEST).entity("Incorrect or incomplete data entered").build();
			}

		}catch(TheGoldenBookException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error in the user update process").build();
		}
	}











}
