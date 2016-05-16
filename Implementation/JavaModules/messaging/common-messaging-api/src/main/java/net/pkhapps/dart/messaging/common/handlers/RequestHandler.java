package net.pkhapps.dart.messaging.common.handlers;

import net.pkhapps.dart.messaging.common.messages.Request;
import net.pkhapps.dart.messaging.common.messages.Response;

public interface RequestHandler<REQ extends Request<RESP>, RESP extends Response> extends MessageHandler<REQ> {

    RESP handleRequest(REQ request);
}
