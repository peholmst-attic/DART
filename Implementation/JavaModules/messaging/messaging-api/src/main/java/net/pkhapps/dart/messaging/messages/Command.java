package net.pkhapps.dart.messaging.messages;

public interface Command extends Message {

    interface Builder<M extends Command, B extends Builder<M, B>> extends Message.Builder<M, B> {
    }
}
