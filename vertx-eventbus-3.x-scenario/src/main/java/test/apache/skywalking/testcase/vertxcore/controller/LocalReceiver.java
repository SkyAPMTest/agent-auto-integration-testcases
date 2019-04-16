package test.apache.skywalking.testcase.vertxcore.controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import test.apache.skywalking.testcase.vertxcore.util.CustomMessage;

public class LocalReceiver extends AbstractVerticle {

    @Override
    public void start() {
        EventBus eventBus = getVertx().eventBus();
        eventBus.consumer("local-message-receiver", message -> {
            CustomMessage replyMessage = new CustomMessage(200,
                    "a00000002", "Message sent from local receiver!");
            message.reply(replyMessage);
        });
    }
}
