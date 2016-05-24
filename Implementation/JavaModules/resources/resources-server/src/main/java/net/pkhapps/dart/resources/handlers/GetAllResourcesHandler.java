package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.database.DSLContextFactory;
import net.pkhapps.dart.messaging.handlers.RequestHandler;
import net.pkhapps.dart.resources.messages.AllResources;
import net.pkhapps.dart.resources.messages.GetAllResources;

import java.util.Objects;

public class GetAllResourcesHandler implements RequestHandler<GetAllResources, AllResources> {

    private final DSLContextFactory dslContextFactory;

    public GetAllResourcesHandler(DSLContextFactory dslContextFactory) {
        this.dslContextFactory = Objects.requireNonNull(dslContextFactory, "dslContextFactory must not be null");
    }

    @Override
    public AllResources handleRequest(GetAllResources request) {
        return null;
    }
}
