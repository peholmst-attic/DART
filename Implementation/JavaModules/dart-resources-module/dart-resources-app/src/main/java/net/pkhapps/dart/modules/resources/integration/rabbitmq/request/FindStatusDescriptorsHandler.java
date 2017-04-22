package net.pkhapps.dart.modules.resources.integration.rabbitmq.request;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import net.pkhapps.dart.modules.resources.integration.xsd.FindStatusDescriptors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by peholmst on 22/04/2017.
 */
@ApplicationScoped
class FindStatusDescriptorsHandler
        implements RequestHandler<FindStatusDescriptors.Request, FindStatusDescriptors.Response> {

    @NotNull
    @Override
    public FindStatusDescriptors.Response handleRequest(@NotNull FindStatusDescriptors.@NotNull Request request,
                                                        @Nullable String userId) throws AccessDeniedException {
        return null;
    }
}
