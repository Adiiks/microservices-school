package pl.adrianpacholak.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import pl.adrianpacholak.gatewayserver.converter.KeycloakRoleConverter;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.authorizeExchange(exchange -> {
            // USER-SERVICE
            exchange.pathMatchers(HttpMethod.POST, "/user-service/students").hasRole("ADMIN");
            exchange.pathMatchers(HttpMethod.POST, "/user-service/teachers").hasRole("ADMIN");

            // FACULTY-SERVICE
            exchange.pathMatchers(HttpMethod.POST, "/faculty-service/faculties").hasRole("ADMIN");

            // COURSE-SERVICE
            exchange.pathMatchers(HttpMethod.POST, "/course-service/courses").hasRole("ADMIN");

            // LESSON-SERVICE
            exchange.pathMatchers(HttpMethod.POST, "/lesson-service/lessons").hasRole("ADMIN");
            exchange.pathMatchers(HttpMethod.PUT, "/lesson-service/lessons/registrations/register/{registrationId}").hasRole("STUDENT");
            exchange.pathMatchers(HttpMethod.POST, "/lesson-service/lessons/registrations").hasRole("ADMIN");
            exchange.pathMatchers(HttpMethod.GET, "/lesson-service/lessons/registrations").hasAnyRole("ADMIN", "STUDENT");

            // ATTENDANCE-SERVICE
            exchange.pathMatchers(HttpMethod.POST, "/attendance-service/attendance-lists").hasRole("TEACHER");

            // GENERAL
            exchange.anyExchange().authenticated();
        })
                .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                        oAuth2ResourceServerSpec.jwt(jwtSpec ->
                                jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesConverter())));

        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);

        return serverHttpSecurity.build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
