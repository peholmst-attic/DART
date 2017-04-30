package net.pkhapps.dart.modules.resources.integration.rabbitmq.request;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.HandlerException;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.HandlerNotFoundException;
import net.pkhapps.dart.modules.resources.integration.rabbitmq.RequestBroker;
import net.pkhapps.dart.modules.resources.integration.xsd.Request;
import net.pkhapps.dart.modules.resources.integration.xsd.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Objects;

/**
 * Default implementation of {@link RequestBroker}. The {@link HandlerFor} qualifier is used to select which request
 * handler to invoke.
 */
@ApplicationScoped
class DefaultRequestBroker implements RequestBroker {

    @Inject
    @Any
    Instance<RequestHandler> requestHandlers;

    @Override
    @NotNull
    public Response handleRequest(@NotNull Request request, @Nullable String userId)
            throws HandlerNotFoundException, HandlerException, AccessDeniedException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            RequestHandler requestHandler = requestHandlers.select(new HandlerForQualifier(request.getClass())).get();
            try {
                return requestHandler.handleRequest(request, userId);
            } catch (RuntimeException ex) {
                throw new HandlerException(ex.getMessage(), ex);
            }
        } catch (RuntimeException ex) {
            throw new HandlerNotFoundException();
        }
    }
}
