package net.pkhapps.dart.messaging.messages;

import java.time.Instant;
import java.util.Objects;

public abstract class Broadcast extends Message {

    public Broadcast(Instant timestamp, String conversationId) {
        super(Objects.requireNonNull(timestamp), conversationId);
    }
}
