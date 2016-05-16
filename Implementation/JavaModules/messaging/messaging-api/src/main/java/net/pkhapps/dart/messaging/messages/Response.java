package net.pkhapps.dart.messaging.messages;

public interface Response extends Message {

    interface Builder<M extends Response, B extends Builder<M, B>> extends Message.Builder<M, B> {
    }
}
