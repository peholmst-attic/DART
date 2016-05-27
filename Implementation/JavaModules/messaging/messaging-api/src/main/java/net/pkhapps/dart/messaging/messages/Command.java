package net.pkhapps.dart.messaging.messages;

import java.time.Instant;
import java.util.Objects;

public abstract class Command extends Message {

    public Command(Instant timestamp, String conversationId) {
        super(Objects.requireNonNull(timestamp), conversationId);
    }
}
