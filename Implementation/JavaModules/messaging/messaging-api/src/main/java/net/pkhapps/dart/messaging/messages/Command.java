package net.pkhapps.dart.messaging.messages;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

public abstract class Command extends Message {

    public Command(@NotNull Instant timestamp, @Nullable String conversationId) {
        super(Objects.requireNonNull(timestamp, "timestamp must not be null"), conversationId);
    }
}
