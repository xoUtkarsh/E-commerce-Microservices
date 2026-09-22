package com.ecommerce.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    KeyResolver hostNameKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        System.out.println("Hello check");
        return builder.routes()
                .route("product-service",r->
                        r.path("/products/**")
                                .filters(f->f.rewritePath("/products(?<segment>/?.*)",
                                        "/api/products${segment}"))
                                .uri("lb://PRODUCT-SERVICE"))
                .route("user-service",r->
                        r.path("/users/**")
                                .filters(f->f.rewritePath("/users(?<segment>/?.*)",
                                        "/api/users${segment}"))
                                .uri("lb://USER-SERVICE"))
                .route("order-service",r->
                        r.path("/orders/**","/cart/**")
                                .filters(f->f.rewritePath("/(?<segment>.*)",
                                        "/api/${segment}"))
                                .uri("lb://ORDER-SERVICE"))
                .route("eureka-server",r->
                        r.path("/eureka/main")
                                .filters(f->f.rewritePath("/eureka/main","/"))
                                .uri("http://localhost:8761"))
                .route("eureka-server-static",r->
                        r.path("/eureka/**")
                                .uri("http://localhost:8761"))
                .build();
    }
}
