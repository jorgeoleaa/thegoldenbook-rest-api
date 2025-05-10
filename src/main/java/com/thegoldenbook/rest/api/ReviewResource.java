package com.thegoldenbook.rest.api;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thegoldenbook.TheGoldenBookException;
import com.thegoldenbook.dao.DataException;
import com.thegoldenbook.model.Results;
import com.thegoldenbook.model.Review;
import com.thegoldenbook.service.ReviewService;
import com.thegoldenbook.service.impl.ReviewServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/review")
@Singleton
public class ReviewResource {

	private ReviewService reviewService = null;

	private static Logger logger = LogManager.getLogger(ReviewResource.class);

	public ReviewResource() {
		reviewService = new ReviewServiceImpl();
	}

	@POST
	@Path("/{locale}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId="createReview",
			summary = "Create a review",
			description = "Creates a review for a book associated with a customer.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Review created successfully."
							),
					@ApiResponse(
							responseCode = "400",
							description = "Incorrect or incomplete data entered."
							),
					@ApiResponse(
							responseCode = "500",
							description = "Error creating the review."
							)
			}
			)

	public Response create(@PathParam("locale") String locale, Review review){

		try {
			review.setPublicationDate(new Date());
			reviewService.create(review, locale);
			Review reviewCreated = reviewService.findByReview(review.getUserId(), review.getBookId(), locale);
			return Response.status(Status.OK).entity(reviewCreated).build();
		} catch (DataException de) {
			logger.error(de.getMessage(), de);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error in the review create process")
					.build();
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId="deleteReview",
			summary = "Delete a review",
			description = "Deletes a review by specifying the associated book ID and the ID of the customer who made it.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "The review was successfully deleted."
							),
					@ApiResponse(
							responseCode = "400",
							description = "The request is invalid or the review does not exist."
							),
					@ApiResponse(
							responseCode = "500",
							description = "An internal error occurred while trying to delete the review."
							)
			}
			)

	public Response delete(
			@QueryParam("bookIdId") Long bookId,
			@QueryParam("userId") Long userId
			) {
		if (bookId == null || userId == null) {
			return Response.status(Status.BAD_REQUEST)
					.entity("All fields are required and cannot be empty.")
					.build();
		}

		try {
			boolean deleted = reviewService.delete(userId, bookId);
			if (deleted) {
				return Response.status(Status.OK)
						.entity("Review successfully deleted")
						.build();
			} else {
				return Response.status(Status.BAD_REQUEST)
						.entity("Review not found for deletion")
						.build();
			}
		} catch (DataException e) {
			logger.error("Error in the review deletion process: {}", e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("An error occurred while processing the request.")
					.build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "findReviewsByBook",
			summary = "Search book reviews",
			description = "Searches for all reviews belonging to the book with the provided ID",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Reviews found",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = Review[].class)
									)
							),
					@ApiResponse(
							responseCode = "404",
							description = "No results found"
							),
					@ApiResponse(
							responseCode = "400",
							description = "Error during data retrieval process"
							)
			}
			)

	public Response findByBook(
			@QueryParam("bookId") Long bookId,
			@QueryParam("locale") String locale
			) {

		Results<Review> reviews = null;
		try {

			reviews = reviewService.findByBook(bookId, 1, Integer.MAX_VALUE, locale);
			return Response.ok(reviews.getPage()).build();
		}catch(TheGoldenBookException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error during the process of retrieving reviews")
					.build();
		}
	}
}
