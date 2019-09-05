package test.apache.skywalking.apm.testcase.webflux.config;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author zhaoyuguang
 */

@Component
public class CustomFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(new MyServerWebExchangeDecorator(exchange));
    }

    public static class MyServerWebExchangeDecorator extends ServerWebExchangeDecorator {
        public MyServerWebExchangeDecorator(ServerWebExchange delegate) {
            super(delegate);
        }
    }

}
