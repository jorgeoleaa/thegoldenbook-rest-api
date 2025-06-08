package com.thegoldenbook.rest.api.ratelimit;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class RateLimitFilter implements ContainerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Creates a bucket with a capacity of 3 tokens,
     * refilling 3 tokens every 20 seconds (full recharge).
     */
    private Bucket newBucket() {
        Bandwidth limit = Bandwidth.builder()
                                   .capacity(3)
                                   .refillGreedy(3, Duration.ofSeconds(20))
                                   .build();
        return Bucket.builder()
                     .addLimit(limit)
                     .build();
    }

    private Bucket resolveBucket(String ip) {
        return buckets.computeIfAbsent(ip, k -> newBucket());
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        UriInfo uriInfo = requestContext.getUriInfo();
        String ipHeader = requestContext.getHeaderString("X-Forwarded-For");
        String clientIp;

        if (ipHeader != null && !ipHeader.isBlank()) {
            clientIp = ipHeader.split(",")[0].trim();
        } else {
            clientIp = uriInfo.getRequestUri().getHost();
        }

        Bucket bucket = resolveBucket(clientIp);
        if (bucket.tryConsume(1)) {
            return;
        }

        requestContext.abortWith(
            Response.status(429)
                    .entity("Too many requests - please slow down.")
                    .header("Retry-After", 20)
                    .build()
        );
    }
}
