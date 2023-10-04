package pl.adrianpacholak.gatewayserver.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import pl.adrianpacholak.gatewayserver.utils.JwtUtils;

@Component
public class UsernameRequestHeaderFilter extends AbstractGatewayFilterFactory<Object> {

    private final JwtUtils jwtUtils;

    public UsernameRequestHeaderFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
            String username = jwtUtils.getUsername(exchange.getRequest().getHeaders());

            exchange.getRequest().mutate().header("username", username);

            return chain.filter(exchange);
        });
    }
}
