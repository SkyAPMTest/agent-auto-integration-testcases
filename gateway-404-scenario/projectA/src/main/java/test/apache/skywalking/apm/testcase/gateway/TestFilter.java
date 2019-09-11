package test.apache.skywalking.apm.testcase.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author zhaoyuguang
 */

@Component
public class TestFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest newRequest = request.mutate().header("test", "test").build();
        ServerWebExchange nExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(nExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
