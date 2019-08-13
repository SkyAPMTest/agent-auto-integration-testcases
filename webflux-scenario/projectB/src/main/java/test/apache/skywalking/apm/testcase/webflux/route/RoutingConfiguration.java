package test.apache.skywalking.apm.testcase.webflux.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

/**
 * @author zhaoyuguang
 */
@Configuration
public class RoutingConfiguration {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(TestHandler testHandler) {
        return RouterFunctions.route(GET("/testcase/route/{test}"), testHandler::test);
    }

}
