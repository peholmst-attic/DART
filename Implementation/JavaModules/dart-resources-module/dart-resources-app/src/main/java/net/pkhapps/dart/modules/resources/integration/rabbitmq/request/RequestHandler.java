package net.pkhapps.dart.modules.resources.integration.rabbitmq.request;

import net.pkhapps.dart.modules.resources.integration.rabbitmq.AccessDeniedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me!
 */
interface RequestHandler<REQUEST, RESPONSE> {

    @NotNull
    RESPONSE handleRequest(@NotNull REQUEST request, @Nullable String userId) throws AccessDeniedException;
}
