package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.messaging.handlers.RequestHandler;
import net.pkhapps.dart.resources.messages.AllResourceStates;
import net.pkhapps.dart.resources.messages.GetAllResourceStates;

public class GetAllResourceStatesHandler implements RequestHandler<GetAllResourceStates, AllResourceStates> {

    @Override
    public AllResourceStates handleRequest(GetAllResourceStates request) {
        return null;
    }
}
