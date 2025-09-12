package com.mendes.gatewayserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		List<String> services = List.of(
				"accounts",
				"loans",
				"cards");
		RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();

		services.forEach(service -> routes.route(
			p -> p.path("/eazybank/" + service + "/**")
				  .filters(
					f -> f.rewritePath("/eazybank/" + service + "/(?<segment>.*)", "/${segment}")
						  .addResponseHeader("X-Response-Time", 
												LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
				.uri("lb://" + service)));

		return routes.build();
	}

}
