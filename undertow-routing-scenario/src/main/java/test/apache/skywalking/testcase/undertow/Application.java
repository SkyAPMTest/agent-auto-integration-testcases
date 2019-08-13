package test.apache.skywalking.testcase.undertow;

import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import io.undertow.util.Methods;

public class Application {

    public static void main(String[] args) {
        RoutingHandler handler = new RoutingHandler();
        handler.add(Methods.GET, "/projects/{projectId}", exchange -> {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Hello World");
        });
        Undertow server = Undertow.builder()
                .addHttpListener(18081, "0.0.0.0")
                .setHandler(handler).build();
        server.start();
    }

}
