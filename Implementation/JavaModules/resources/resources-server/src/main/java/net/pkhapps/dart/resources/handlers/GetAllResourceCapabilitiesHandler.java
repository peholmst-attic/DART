package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.messaging.handlers.RequestHandler;
import net.pkhapps.dart.resources.messages.AllResourceCapabilities;
import net.pkhapps.dart.resources.messages.GetAllResourceCapabilities;

public class GetAllResourceCapabilitiesHandler implements RequestHandler<GetAllResourceCapabilities, AllResourceCapabilities> {

    @Override
    public AllResourceCapabilities handleRequest(GetAllResourceCapabilities request) {
        return null;
    }
}
