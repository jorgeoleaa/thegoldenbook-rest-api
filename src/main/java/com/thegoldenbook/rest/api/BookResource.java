package com.thegoldenbook.rest.api;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thegoldenbook.TheGoldenBookException;
import com.thegoldenbook.model.Book;
import com.thegoldenbook.model.Results;
import com.thegoldenbook.service.BookCriteria;
import com.thegoldenbook.service.BookService;
import com.thegoldenbook.service.impl.BookServiceImpl;

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

@Path("/book")
public class BookResource {

	private BookService bookService = null;
	
	private static Logger logger = LogManager.getLogger(BookResource.class);
	
	public BookResource() {
		bookService = new BookServiceImpl();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
	    operationId = "findBooksByCriteria",
	    summary = "Search for books by criteria",
	    description = "Retrieves a list of books based on the provided criteria",
	    responses = {
	        @ApiResponse(
	            responseCode = "200",
	            description = "Books found",
	            content = @Content(
	                mediaType = MediaType.APPLICATION_JSON,
	                schema = @Schema(implementation = Book[].class)
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
	public Response findByCriteria(
			@QueryParam("id") Long id,
			@QueryParam("isbn") String isbn,
			@QueryParam("title") String title,
			@QueryParam("minPrice") Double minPrice,
			@QueryParam("maxPrice") Double maxPrice,
			@QueryParam("minUnits") Integer minUnits,
			@QueryParam("maxUnits") Integer maxUnits,
			@QueryParam("startDate") Date startDate,
			@QueryParam("endDate") Date endDate,
			@QueryParam("readingAgeGroupId") Integer readingAgeGroupId,
			@QueryParam("languageId") Integer languageId,
			@QueryParam("formatId") Integer formatId,
			@QueryParam("locale") String locale){
		
		BookCriteria criteria = new BookCriteria();
		
		criteria.setId(id);
		criteria.setIsbn(isbn);
		criteria.setTitle(title);
		criteria.setMinPrice(minPrice);
		criteria.setMaxPrice(maxPrice);
		criteria.setMinUnits(minUnits);
		criteria.setMaxUnits(maxUnits);
		criteria.setStartDate(startDate);
		criteria.setEndDate(endDate);
		criteria.setReadingAgeGroupId(readingAgeGroupId);
		criteria.setLanguageId(languageId);
		criteria.setFormatId(formatId);
		criteria.setLocale(locale);
		
		Results<Book> books = null;
		
		try {
			
			books = bookService.findByCriteria(criteria, 1, Integer.MAX_VALUE);
			
		}catch(TheGoldenBookException pe) {
			logger.error(pe.getMessage(), pe);
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Error in the books search process")
					.build();
		}
		
		logger.info(books.getPage());
		
		return Response.ok(books.getPage()).build();
	}
	
	
}
