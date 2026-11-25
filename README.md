# The Golden Book · REST API

REST API behind **The Golden Book**, a platform for selling new and antique books. It exposes the catalogue, users, orders and reviews to the [web SPA](https://github.com/jorgeoleaa/thegoldenbook-spa) and the [mobile app](https://github.com/jorgeoleaa/thegoldenbook-mobile), on top of the shared [business and data-access layer](https://github.com/jorgeoleaa/thegoldenbook-middleware).

> **Status:** this is the JAX-RS version. The API is currently being rebuilt with Spring Boot and Docker.

## Highlights

- **JAX-RS with Jersey**, packaged as a WAR and deployed on a servlet container.
- **Interactive documentation** with OpenAPI annotations and a bundled Swagger UI.
- **Rate limiting** per client with Bucket4j, implemented as a JAX-RS request filter that answers `429 Too Many Requests` when the limit is exceeded.
- **Caching with Redis** for frequently requested catalogue queries.
- **Multi-language responses**: most endpoints take a `{locale}` so content is returned in the user's language.
- **Rich search**: books can be filtered by ISBN, title, price range, stock, publication dates, language, format and reading age group.

## Architecture

```
   SPA (React)      Mobile (React Native)
          \              /
           \            /
         REST API  (this repo)
         JAX-RS · Jersey · Swagger
         rate limiting · Redis cache
                 |
         Middleware (Java)
         business logic · JDBC / Hibernate
                 |
               MySQL
```

The API stays thin: resources validate input, apply cross-cutting concerns (rate limiting, caching, CORS) and delegate the business logic to the middleware, which is shared with the server-rendered web app and the desktop back-office.

## Endpoints

Base path: `/api`

| Resource | Path | Operations |
|---|---|---|
| Books | `/book` | Search with filters |
| Book images | `/image/{bookId}/image` | Get a book's cover |
| Users | `/user/{locale}/…` | Register, authenticate, read, update, delete |
| Orders | `/order` | Read, create, update, delete |
| Reviews | `/review` | Read, create, delete |
| Catalogue data | `/format`, `/language`, `/genre`, `/age` | Formats, languages, literary genres and reading age groups |

The full, interactive specification is available in Swagger UI at `/swagger-ui/` once the application is running.

## Tech stack

Java · JAX-RS (Jersey) · OpenAPI / Swagger · Bucket4j · Redis (Jedis) · Gson · Log4j 2 · Maven · MySQL

## Running locally

Requirements: JDK, Maven, a servlet container (e.g. Tomcat), MySQL and Redis.

1. Build and install the [middleware](https://github.com/jorgeoleaa/thegoldenbook-middleware) into your local Maven repository.
2. Configure the database, mail and image paths in `thegoldenbook-cfg.properties`.
3. Build the WAR:
   ```bash
   mvn clean package
   ```
4. Deploy the generated WAR to your servlet container and open `/swagger-ui/` to explore the API.

## Related repositories

- [thegoldenbook-middleware](https://github.com/jorgeoleaa/thegoldenbook-middleware): business and data-access layer
- [thegoldenbook-spa](https://github.com/jorgeoleaa/thegoldenbook-spa): customer-facing web app (React)
- [thegoldenbook-mobile](https://github.com/jorgeoleaa/thegoldenbook-mobile): mobile app (React Native)
- [thegoldenbook-web](https://github.com/jorgeoleaa/thegoldenbook-web): server-rendered web store (Servlets/JSP)
- [thegoldenbook-desktop](https://github.com/jorgeoleaa/thegoldenbook-desktop): back-office desktop app (Swing)
