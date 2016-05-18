package net.pkhapps.dart.messaging.messages;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

public abstract class Response extends Message {

    public Response(@NotNull Instant timestamp, @Nullable String conversationId) {
        super(Objects.requireNonNull(timestamp, "timestamp must not be null"), conversationId);
    }
}
