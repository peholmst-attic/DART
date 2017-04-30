package net.pkhapps.dart.modules.base.rabbitmq.messaging.consumer.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me!
 */
public interface MessageRouter {

    @Nullable
    String getRoutingKey(@NotNull Object message);

    @Nullable
    String getExchange(@NotNull Object message);
}
