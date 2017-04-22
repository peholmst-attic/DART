package net.pkhapps.dart.modules.resources.integration.rabbitmq.request;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.HandlerException;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.HandlerNotFoundException;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.RequestBroker;
import net.pkhapps.dart.modules.resources.integration.xsd.FindResources;
import net.pkhapps.dart.modules.resources.integration.xsd.FindStatusDescriptors;
import net.pkhapps.dart.modules.resources.integration.xsd.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Function;

/**
 * Default implementation of {@link RequestBroker}. To keep things simple, the message is extracted and directed to the
 * correct handler manually (i.e. no fancy auto-detection of handlers).
 */
@ApplicationScoped
class DefaultRequestBroker implements RequestBroker {

    @Inject
    FindStatusDescriptorsHandler findStatusDescriptorsHandler;

    @Inject
    FindResourcesHandler findResourcesHandler;

    @Override
    @NotNull
    public Object handleRequest(@NotNull Message message, @Nullable String userId)
            throws HandlerNotFoundException, HandlerException, AccessDeniedException {

        FindStatusDescriptors.Request findStatusDescriptors =
                getRequest(FindStatusDescriptors::getRequest, message.getFindStatusDescriptors());
        FindResources.Request findResources =
                getRequest(FindResources::getRequest, message.getFindResources());

        if (findStatusDescriptors != null) {
            return handle(findStatusDescriptorsHandler, findStatusDescriptors, userId);
        } else if (findResources != null) {
            return handle(findResourcesHandler, findResources, userId);
        }

        throw new HandlerNotFoundException();
    }

    @Nullable
    private static <REQUEST, MESSAGE> REQUEST getRequest(@NotNull Function<MESSAGE, REQUEST> getRequest,
                                                         @Nullable MESSAGE message) {
        return Optional.ofNullable(message).map(getRequest).orElse(null);
    }

    @NotNull
    private static <REQUEST, RESPONSE> RESPONSE handle(@NotNull RequestHandler<REQUEST, RESPONSE> handler,
                                                       @NotNull REQUEST request, @Nullable String userId)
            throws AccessDeniedException, HandlerException {
        try {
            return handler.handleRequest(request, userId);
        } catch (RuntimeException ex) {
            // TODO Support for custom error codes through an annotation on the exception class
            throw new HandlerException(ex.getMessage(), ex);
        }
    }
}
