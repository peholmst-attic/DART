package net.pkhapps.dart.messaging.messages;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

public abstract class Message {

    private final Instant timestamp;
    private final String conversationId;

    public Message(@Nullable Instant timestamp, @Nullable String conversationId) {
        this.timestamp = timestamp;
        this.conversationId = conversationId;
    }

    @NotNull
    public Optional<Instant> getTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    @NotNull
    public Optional<String> getConversationId() {
        return Optional.ofNullable(conversationId);
    }
}
