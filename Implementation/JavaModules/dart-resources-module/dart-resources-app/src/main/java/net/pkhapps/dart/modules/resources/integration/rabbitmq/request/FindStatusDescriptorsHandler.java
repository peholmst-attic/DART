package net.pkhapps.dart.modules.resources.integration.rabbitmq.request;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import net.pkhapps.dart.modules.resources.integration.xsd.FindStatusDescriptorsRequest;
import net.pkhapps.dart.modules.resources.integration.xsd.Request;
import net.pkhapps.dart.modules.resources.integration.xsd.Response;
import net.pkhapps.dart.modules.resources.integration.xsd.StatusDescriptorsResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;

/**
 * TODO Document me!
 */
@ApplicationScoped
@HandlerFor(FindStatusDescriptorsRequest.class)
class FindStatusDescriptorsHandler implements RequestHandler {

    @NotNull
    @Override
    public Response handleRequest(@NotNull Request request, @Nullable String userId) throws AccessDeniedException {
        return new StatusDescriptorsResponse();
    }
}
