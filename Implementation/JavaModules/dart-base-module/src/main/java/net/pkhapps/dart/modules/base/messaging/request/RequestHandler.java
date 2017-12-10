package net.pkhapps.dart.modules.base.messaging.request;

import io.reactivex.Single;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me!
 */
public interface RequestHandler<REQ extends Request<RESP>, RESP extends Response> {

    @NotNull
    Single<RESP> handleRequest(@NotNull REQ request, @Nullable String userId);
}
