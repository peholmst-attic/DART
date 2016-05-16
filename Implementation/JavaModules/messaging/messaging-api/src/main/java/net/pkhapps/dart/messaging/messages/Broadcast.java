package net.pkhapps.dart.messaging.messages;

public interface Broadcast extends Message {

    interface Builder<M extends Broadcast, B extends Builder<M, B>> extends Message.Builder<M, B> {
    }
}
