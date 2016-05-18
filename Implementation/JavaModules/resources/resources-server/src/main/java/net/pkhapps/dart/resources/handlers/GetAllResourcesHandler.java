package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.messaging.handlers.RequestHandler;
import net.pkhapps.dart.resources.messages.AllResources;
import net.pkhapps.dart.resources.messages.GetAllResources;

public class GetAllResourcesHandler implements RequestHandler<GetAllResources, AllResources> {

    @Override
    public AllResources handleRequest(GetAllResources request) {
        return null;
    }
}
