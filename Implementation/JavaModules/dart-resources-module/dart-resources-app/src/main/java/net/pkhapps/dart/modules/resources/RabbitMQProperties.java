package net.pkhapps.dart.modules.resources;

import java.util.function.Supplier;

/**
 * TODO Document me!
 */
public interface RabbitMQProperties extends net.pkhapps.dart.modules.base.rabbitmq.RabbitMQProperties {

    Supplier<String> getMessageExpiration();

    Supplier<String> getMessageAppId();
}
