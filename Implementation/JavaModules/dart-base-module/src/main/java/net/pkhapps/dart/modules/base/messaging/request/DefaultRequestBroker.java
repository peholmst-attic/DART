package net.pkhapps.dart.modules.base.messaging.request;

import io.reactivex.Single;
import net.pkhapps.dart.modules.base.messaging.HandlerNotFoundException;
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
public class DefaultRequestBroker implements RequestBroker {

    @Inject
    @Any
    Instance<RequestHandler<?, ?>> requestHandlers;

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <REQ extends Request<RESP>, RESP extends Response> Single<RESP> handleRequest(@NotNull REQ request,
                                                                                         @Nullable String userId)
            throws HandlerNotFoundException {
        Objects.requireNonNull(request, "request must not be null");
        RequestHandler requestHandler;
        try {
            requestHandler = requestHandlers.select(new HandlerForQualifier(request.getClass())).get();
        } catch (RuntimeException ex) {
            throw new HandlerNotFoundException();
        }
        return requestHandler.handleRequest(request, userId);
    }
}
