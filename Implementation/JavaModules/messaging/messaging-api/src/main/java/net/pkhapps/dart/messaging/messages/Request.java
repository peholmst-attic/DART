package net.pkhapps.dart.messaging.messages;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public abstract class Request<R extends Response> extends Message {

    public Request(@Nullable Instant timestamp, @Nullable String conversationId) {
        super(timestamp, conversationId);
    }
}
