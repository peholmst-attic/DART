package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.messaging.handlers.RequestHandler;
import net.pkhapps.dart.resources.messages.CurrentResourceStatus;
import net.pkhapps.dart.resources.messages.GetCurrentResourceStatus;

public class GetCurrentResourceStatusHandler implements RequestHandler<GetCurrentResourceStatus, CurrentResourceStatus> {

    @Override
    public CurrentResourceStatus handleRequest(GetCurrentResourceStatus request) {
        return null;
    }
}
