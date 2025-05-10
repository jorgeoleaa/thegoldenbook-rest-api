package com.thegoldenbook.rest.api;

import org.glassfish.jersey.server.ResourceConfig;

import com.thegoldenbook.rest.api.param.DateParamConverterProvider;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.ApplicationPath;

// In Servlet 3.0, Application is a base class provided by JAX-RS 
// (Java API for RESTful Web Services) to define a RESTful application 
// without the need for a web.xml file.
// ResourceConfig is a Jersey subclass of Application for
// configuring Jersey aspects that used to be defined in web.xml.

@OpenAPIDefinition(
		info = @Info(
				title = "The Golden Book API",
				version = "1.0",
				description = "API for a book-selling business",
				contact = @Contact(
						name = "API Support",
						email = "support@thegoldenbook.com",
						url = "https://thegoldenbook.com"
						),
				license = @License(
						name = "MIT",
						url = "https://opensource.org/licenses/MIT"
						)
				),
		servers = {
				@Server(url = "http://localhost:8080/thegoldenbook-rest-api/", description = "Local Server")
				// When deploying to hosting
				// @Server(url = "https://api.thegoldenbook.com", description = "Production Server"),
		}
		)

@ApplicationPath("/api") // Base context for the endpoints
public class TheGoldenBookRestApplication extends ResourceConfig {

	public TheGoldenBookRestApplication() {
		packages(TheGoldenBookRestApplication.class.getPackage().getName());

		// Swagger annotations to document the API
		register(io.swagger.v3.jaxrs2.integration.resources.OpenApiResource.class);

		register(DateParamConverterProvider.class);

		// https://github.com/swagger-api/swagger-ui?tab=readme-ov-file
		// If you are looking for plain ol' HTML/JS/CSS, download the latest release and copy the contents of the /dist folder to your server.
		// The link points to:
		// https://github.com/swagger-api/swagger-ui/releases/tag/v5.18.3
		// Configure the API URL in swagger-initializer.js
		// Open localhost:8080/<context>/swagger-ui
	}
}
