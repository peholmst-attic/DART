package net.pkhapps.dart.messaging.handlers;

import net.pkhapps.dart.messaging.messages.Request;
import net.pkhapps.dart.messaging.messages.Response;

public interface RequestHandler<REQ extends Request<RESP>, RESP extends Response> extends MessageHandler<REQ> {

    RESP handleRequest(REQ request);
}
