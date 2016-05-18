package net.pkhapps.dart.messaging.messages;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public abstract class FireAndForgetCommand extends Command {

    public FireAndForgetCommand(@NotNull Instant timestamp, @Nullable String conversationId) {
        super(timestamp, conversationId);
    }
}
