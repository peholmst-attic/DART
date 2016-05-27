package net.pkhapps.dart.messaging.messages;

import java.time.Instant;

public abstract class Request<R extends Response> extends Message {

    public Request(Instant timestamp, String conversationId) {
        super(timestamp, conversationId);
    }
}
