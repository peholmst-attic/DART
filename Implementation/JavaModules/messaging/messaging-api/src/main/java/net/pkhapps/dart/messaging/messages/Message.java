package net.pkhapps.dart.messaging.messages;

import java.time.Instant;
import java.util.Optional;

public abstract class Message {

    private final Instant timestamp;
    private final String conversationId;

    public Message(Instant timestamp, String conversationId) {
        this.timestamp = timestamp;
        this.conversationId = conversationId;
    }

    public Optional<Instant> getTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    public Optional<String> getConversationId() {
        return Optional.ofNullable(conversationId);
    }
}
