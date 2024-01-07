package com.example.gatewayservice;

import com.example.gatewayservice.filter.AuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;


@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}
	@Bean
	DiscoveryClientRouteDefinitionLocator locator(ReactiveDiscoveryClient rdc, DiscoveryLocatorProperties dlp){
		return new DiscoveryClientRouteDefinitionLocator(rdc,dlp);
	}
	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder rlb, AuthenticationFilter
			authorizationHeaderFilter) {

		return rlb
				.routes()
				.route(p -> p
						.path("/AUTH-SERVICE/auth/*")
						.filters(f -> f.removeRequestHeader("Cookie")
								.rewritePath("/AUTH-SERVICE/(?<segment>.*)", "/$\\{segment}")
								.filter(authorizationHeaderFilter.apply(new
										AuthenticationFilter.Config())))
						.uri("lb://AUTH-SERVICE")
				)
				.route(p -> p
						.path("/ACCOUNT-SERVICE/*")
						.filters(f -> f.removeRequestHeader("Cookie")
								.rewritePath("/ACCOUNT-SERVICE/(?<segment>.*)", "/$\\{segment}")
								.filter(authorizationHeaderFilter.apply(new
										AuthenticationFilter.Config())))
						.uri("lb://ACCOUNT-SERVICE")
				)
				.route(p -> p
						.path("/OPERATION-SERVICE/*")
						.filters(f -> f.removeRequestHeader("Cookie")
								.rewritePath("/OPERATION-SERVICE/(?<segment>.*)", "/$\\{segment}")
								.filter(authorizationHeaderFilter.apply(new
										AuthenticationFilter.Config())))
						.uri("lb://OPERATION-SERVICE")
				)
				.build();
	}


}
