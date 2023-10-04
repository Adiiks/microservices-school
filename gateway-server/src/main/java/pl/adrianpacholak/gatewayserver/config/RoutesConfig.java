package pl.adrianpacholak.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.adrianpacholak.gatewayserver.filter.UsernameRequestHeaderFilter;

@Configuration
public class RoutesConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, UsernameRequestHeaderFilter usernameFilter) {
        return builder.routes()
                .route("lesson-service", r -> r.path("/lesson-service/lessons/**")
                        .filters(f ->  f.rewritePath("/lesson-service/(?<segment>.*)", "/${segment}")
                                .filter(usernameFilter.apply(new Object())))
                        .uri("lb://LESSON-SERVICE"))
                .build();
    }
}
