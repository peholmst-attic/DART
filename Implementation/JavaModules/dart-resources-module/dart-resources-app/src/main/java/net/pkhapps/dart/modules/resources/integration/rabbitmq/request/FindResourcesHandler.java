package net.pkhapps.dart.modules.resources.integration.rabbitmq.request;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import net.pkhapps.dart.modules.resources.integration.xsd.FindResources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;

/**
 * TODO Document me!
 */
@ApplicationScoped
class FindResourcesHandler implements RequestHandler<FindResources.Request, FindResources.Response> {

    @Override
    @NotNull
    public FindResources.Response handleRequest(@NotNull FindResources.Request request,
                                                @Nullable String userId) throws AccessDeniedException {
        return null;
    }
}
