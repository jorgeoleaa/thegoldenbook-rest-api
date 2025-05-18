package com.thegoldenbook.rest.api;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thegoldenbook.TheGoldenBookException;
import com.thegoldenbook.dao.DataException;
import com.thegoldenbook.model.Order;
import com.thegoldenbook.service.MailException;
import com.thegoldenbook.service.OrderCriteria;
import com.thegoldenbook.service.OrderService;
import com.thegoldenbook.service.impl.OrderServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/order")
public class OrderResource {

	private OrderService orderService = null; 

	private static Logger logger = LogManager.getLogger(OrderResource.class	);

	public OrderResource() {
		orderService = new OrderServiceImpl();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "findOrdersByCriteria",
			summary = "Search orders by criteria",
			description = "Search orders based on several entered parameters",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Orders found",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = Order[].class)
									)
							),
					@ApiResponse(
							responseCode = "400",
							description = "Incorrect data entered"
							),
					@ApiResponse(
							responseCode = "500",
							description = "Error processing the request"
							)
			}
			)

	public Response findByCriteria(
			@QueryParam("id") Long id,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate,
			@QueryParam("minPrice") Double minPrice,
			@QueryParam("maxPrice") Double maxPrice,
			@QueryParam("userId") Long userId,
			@QueryParam("orderStatusId") Integer orderStatusId) {


		OrderCriteria orderCriteria = new OrderCriteria();
		orderCriteria.setId(id);
		orderCriteria.setMinPrice(minPrice);
		orderCriteria.setMaxPrice(maxPrice);
		orderCriteria.setUserId(userId);
		orderCriteria.setOrderStatusId(orderStatusId);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (startDate != null) {
				orderCriteria.setStartDate(formatter.parse(startDate));
			}
			if (endDate != null) {
				orderCriteria.setEndDate(formatter.parse(endDate));
			}
		} catch (Exception pe) {
			logger.error("Error parsing the date: " + pe.getMessage(), pe);
			return Response.status(Status.BAD_REQUEST)
					.entity("Invalid date format. Use yyyy-MM-dd.")
					.build();
		}

		try {
			List<Order> result = orderService.findByCriteria(orderCriteria, 1, Integer.MAX_VALUE).getPage();
			return Response.status(Status.OK).entity(result).build();
		} catch (DataException de) {
			logger.error("Data error: " + de.getMessage(), de);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error in the process of searching for orders.")
					.build();
		}
	}

	@POST
	@Path("/{locale}/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
	    operationId = "createOrder",
	    summary = "Create an order",
	    description = "Creates an order by entering all its data",
	    responses = {
	        @ApiResponse(
	            responseCode = "200",
	            description = "The order was created successfully",
	            content = @Content(
	                mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Order.class)
	            )
	        ),
	        @ApiResponse(
	            responseCode = "400",
	            description = "Error sending the order creation email"
	        )
	    }
	)
	public Response create(@PathParam("locale") String locale, Order order) {
	    try {
	        Long id = orderService.create(order, locale);
	        Order createdOrder = orderService.findBy(id, locale);
	        return Response.status(Status.OK).entity(createdOrder).build();
	    } catch (DataException de) {
	        logger.error(de.getMessage(), de);
	        return Response.status(Status.BAD_REQUEST).entity("Error in the process of creating the order.").build();
	    } catch (MailException me) {
	        logger.error("Error sending the email", me.getMessage(), me);
	        return Response.status(Status.BAD_REQUEST).entity("Error sending the order creation email").build();
	    }
	}


	@DELETE
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "deleteOrder",
			summary = "Delete an order",
			description = "Deletes an order based on the provided identifier",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Order deleted successfully"
							),
					@ApiResponse(
							responseCode = "400",
							description = "Error in the order deletion process"
							)
			}
			)  

	public Response delete(@QueryParam("id") Long id) {

		try {
			orderService.delete(id);
			return Response.status(Status.OK).entity("Order successfully deleted.").build();
		}catch(DataException de) {
			logger.error(de.getMessage(), de);
			return Response.status(Status.BAD_REQUEST).entity("Error in the order deletion process").build();
		}

	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "updateOrder",
			summary = "Update an order",
			description = "Updates all data related to the order",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Order updated successfully",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = Order.class)
									)
							),
					@ApiResponse(
							responseCode = "400",
							description = "Failed to update the order"
							),
					@ApiResponse(
							responseCode = "500",
							description = "Internal error during the order update process"
							)
			}
			)  

	public Response update(Order order) {

		try {
			if(orderService.update(order)) {
				String locale = "es_ES";
				Order updatedOrder = orderService.findBy(order.getId(), locale);
				return Response.ok().entity(updatedOrder).build();
			}else {
				return Response.status(Status.BAD_REQUEST).entity("Unable to update the order").build();
			}

		}catch(TheGoldenBookException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error in the order update process").build();
		}
	}
}
