package pl.adrianpacholak.gatewayserver.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import pl.adrianpacholak.gatewayserver.utils.JwtUtils;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory {

    private final JwtUtils jwtUtils;

    public AuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
            String username = jwtUtils.getUsername(exchange.getRequest().getHeaders());

            exchange.getRequest().getHeaders().add("username", username);

            return chain.filter(exchange);
        });
    }
}
