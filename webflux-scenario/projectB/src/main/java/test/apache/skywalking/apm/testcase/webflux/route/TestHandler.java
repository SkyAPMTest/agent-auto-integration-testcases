package test.apache.skywalking.apm.testcase.webflux.route;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author zhaoyuguang
 */
@Component
public class TestHandler {

    public Mono<ServerResponse> test(ServerRequest request) {
        System.out.println(request.path());
        if (request.path().contains("error")) {
            throw new RuntimeException("test_error");
        }
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromObject("route"));
    }
}
