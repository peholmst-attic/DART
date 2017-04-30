package net.pkhapps.dart.modules.resources.integration.rabbitmq.request;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import net.pkhapps.dart.modules.resources.integration.xsd.FindResourcesRequest;
import net.pkhapps.dart.modules.resources.integration.xsd.Request;
import net.pkhapps.dart.modules.resources.integration.xsd.ResourcesResponse;
import net.pkhapps.dart.modules.resources.integration.xsd.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;

/**
 * TODO Document me!
 */
@ApplicationScoped
@HandlerFor(FindResourcesRequest.class)
class FindResourcesHandler implements RequestHandler {

    @Override
    @NotNull
    public Response handleRequest(@NotNull Request request, @Nullable String userId) throws AccessDeniedException {
        return new ResourcesResponse();
    }
}
