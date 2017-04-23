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
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
    public Message handleRequest(@NotNull Message message, @Nullable String userId)
            throws HandlerNotFoundException, HandlerException, AccessDeniedException {

        FindStatusDescriptors.Request findStatusDescriptors =
                getRequest(FindStatusDescriptors::getRequest, message.getFindStatusDescriptors());
        FindResources.Request findResources =
                getRequest(FindResources::getRequest, message.getFindResources());

        if (findStatusDescriptors != null) {
            return setResponse(FindStatusDescriptors::new, FindStatusDescriptors::setResponse,
                    Message::setFindStatusDescriptors,
                    handle(findStatusDescriptorsHandler, findStatusDescriptors, userId));
        } else if (findResources != null) {
            return setResponse(FindResources::new, FindResources::setResponse, Message::setFindResources,
                    handle(findResourcesHandler, findResources, userId));
        }

        throw new HandlerNotFoundException();
    }

    @Nullable
    private static <REQUEST, PART> REQUEST getRequest(@NotNull Function<PART, REQUEST> getRequest,
                                                      @Nullable PART PART) {
        return Optional.ofNullable(PART).map(getRequest).orElse(null);
    }

    @NotNull
    private static <RESPONSE, PART> Message setResponse(@NotNull Supplier<PART> createMessagePart,
                                                        @NotNull BiConsumer<PART, RESPONSE> setResponse,
                                                        @NotNull BiConsumer<Message, PART> setMessagePart,
                                                        @NotNull RESPONSE response) {
        PART part = createMessagePart.get();
        setResponse.accept(part, response);
        Message message = new Message();
        setMessagePart.accept(message, part);
        return message;
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
