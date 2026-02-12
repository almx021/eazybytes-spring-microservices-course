package com.mendes.gatewayserver;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
// import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();

		// List<String> services = List.of(
		// 		"accounts",
		// 		"loans",
		// 		"cards");

		// services.forEach(service -> routes.route(
		// 	p -> p.path("/eazybank/" + service + "/**")
		// 		  .filters(
		// 			f -> f.rewritePath("/eazybank/" + service + "/(?<segment>.*)", "/${segment}")
		// 				  .addResponseHeader("X-Response-Time", 
		// 										LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
		// 		.uri("lb://" + service)
		// 	)
		// );

		routes.route("accounts", 
			p -> p
				.path("/eazybank/accounts/**")
				.filters(
					f -> f
						.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
						.addResponseHeader("X-Response-Time", 
												LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
						.circuitBreaker(
							config -> config
								.setName("accountsCircuitBreaker")
								.setFallbackUri("forward:/contactSupport")))
				.uri("lb://accounts"))
		.route("loans", 
			p -> p
				.path("/eazybank/loans/**")
				.filters(
					f -> f
						.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
						.addResponseHeader("X-Response-Time", 
												LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
						.retry(
							retryConfig -> retryConfig
								.setRetries(3)
								.setMethods(HttpMethod.GET)
								.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)))
				.uri("lb://loans"))
		.route("cards", 
			p -> p
				.path("/eazybank/cards/**")
				.filters(
					f -> f
						.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
						.addResponseHeader("X-Response-Time", 
												LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
						// .requestRateLimiter(
						// 	config -> config
						// 		.setRateLimiter(redisRateLimiter())
						// 		.setKeyResolver(userKeyResolver()))
							)
				.uri("lb://cards"));

		return routes.build();
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
			.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
			.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

	// @Bean
	// public RedisRateLimiter redisRateLimiter() {
	// 	return new RedisRateLimiter(1, 1, 1);
	// }

	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
			.defaultIfEmpty("anonymous");
	}
}
