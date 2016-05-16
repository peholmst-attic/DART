package net.pkhapps.dart.messaging.messages;

public interface Request<R extends Response> extends Message {

    interface Builder<M extends Request<R>, R extends Response, B extends Builder<M, R, B>> extends Message.Builder<M, B> {
    }
}
