package net.pkhapps.dart.messaging.common.handlers;

import net.pkhapps.dart.messaging.common.messages.Broadcast;

public interface BroadcastHandler<B extends Broadcast> extends MessageHandler<B> {

    void onBroadcast(B broadcast);
}
