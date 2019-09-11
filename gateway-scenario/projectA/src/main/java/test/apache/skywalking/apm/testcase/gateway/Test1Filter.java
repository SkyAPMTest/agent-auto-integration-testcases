package test.apache.skywalking.apm.testcase.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class Test1Filter implements GlobalFilter, Ordered {
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest buildRequest =  exchange.getRequest().mutate().build();
        return chain.filter(exchange.mutate().request(buildRequest).build());
    }
    public int getOrder() {
        return 0;
    }
}
