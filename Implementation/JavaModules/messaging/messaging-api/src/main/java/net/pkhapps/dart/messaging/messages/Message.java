package net.pkhapps.dart.messaging.messages;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Optional;

public interface Message {

    @NotNull
    Optional<Instant> getTimestamp();

    @NotNull
    Optional<String> getConversationId();

    interface Builder<M extends Message, B extends Builder<M, B>> {

        @NotNull
        B withTimestamp(Instant timestamp);

        @NotNull
        B withConversationId(String conversationId);

        @NotNull
        M build();
    }
}
