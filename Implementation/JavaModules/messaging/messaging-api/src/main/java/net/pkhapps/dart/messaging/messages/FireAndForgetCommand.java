package net.pkhapps.dart.messaging.messages;

import java.time.Instant;

public abstract class FireAndForgetCommand extends Command {

    public FireAndForgetCommand(Instant timestamp, String conversationId) {
        super(timestamp, conversationId);
    }
}
