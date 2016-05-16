package net.pkhapps.dart.messaging.handlers;

import net.pkhapps.dart.messaging.messages.Broadcast;

public interface BroadcastHandler<B extends Broadcast> extends MessageHandler<B> {

    void onBroadcast(B broadcast);
}
