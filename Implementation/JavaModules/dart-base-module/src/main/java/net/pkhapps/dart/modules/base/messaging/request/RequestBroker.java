package net.pkhapps.dart.modules.base.messaging.request;

import io.reactivex.Single;
import net.pkhapps.dart.modules.base.messaging.HandlerNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me!
 */
public interface RequestBroker {

    /**
     * @param request
     * @param userId
     * @return
     * @throws HandlerNotFoundException
     */
    @NotNull <REQ extends Request<RESP>, RESP extends Response> Single<RESP> handleRequest(@NotNull REQ request,
                                                                                           @Nullable String userId)
            throws HandlerNotFoundException;
}
