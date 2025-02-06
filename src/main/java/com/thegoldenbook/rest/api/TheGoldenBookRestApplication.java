package com.thegoldenbook.rest.api;

import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.ApplicationPath;

//Nota profe para alumnado:
//En Servlet 3.0, Application es una clase base proporcionada por JAX-RS 
//(Java API for RESTful Web Services) para definir una aplicación RESTful 
//sin necesidad de un web.xml.
//ResourceConfig es una subclase de Jersey de Application para
//configurar aspectos de jersey que antes iban en el web.xml

@OpenAPIDefinition(
	    info = @Info(
	        title = "The Golden Book API",
	        version = "1.0",
	        description = "API para negocio de venta de libros ",
	        contact = @Contact(
	            name = "Soporte API",
	            email = "soporte@thegoldenbook.com",
	            url = "https://thegoldenbook.com"
	        ),
	        license = @License(
	            name = "MIT",
	            url = "https://opensource.org/licenses/MIT"
	        )
	    ),
	    servers = {
	    		@Server(url = "http://localhost:8080/thegoldenbook-rest-api/", description = "Servidor Local")
	    		// Cuando lo subais al hosting
	    		// @Server(url = "https://api.thegoldenbook.com", description = "Servidor de Producción"),

	    }
)

@ApplicationPath("/api") //contexto de base para los endpoints
public class TheGoldenBookRestApplication extends ResourceConfig{
	
	public TheGoldenBookRestApplication() {
		packages(TheGoldenBookRestApplication.class.getPackage().getName());
		
		//Anotaciones de Swagger para documentar con API
		register(io.swagger.v3.jaxrs2.integration.resources.OpenApiResource.class);
		
		
		// JAL: 
		// https://github.com/swagger-api/swagger-ui?tab=readme-ov-file
//		If you are looking for plain ol' HTML/JS/CSS, download the latest release and copy the contents of the /dist folder to your server.
		// Y el link lleva a:
		// https://github.com/swagger-api/swagger-ui/releases/tag/v5.18.3
		// Configurar en el swagger-initializer.js la URL de la web
		// Abrir localhost:8080/<contexto>/swagger-ui
	}
}
