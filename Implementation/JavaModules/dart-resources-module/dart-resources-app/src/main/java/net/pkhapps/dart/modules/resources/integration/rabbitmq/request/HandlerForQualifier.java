package net.pkhapps.dart.modules.resources.integration.rabbitmq.request;

import net.pkhapps.dart.modules.resources.integration.xsd.Request;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.util.AnnotationLiteral;
import java.util.Objects;

/**
 * TODO Document me!
 */
class HandlerForQualifier extends AnnotationLiteral<HandlerFor> implements HandlerFor {

    private final Class<? extends Request> value;

    HandlerForQualifier(@NotNull Class<? extends Request> value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public Class<? extends Request> value() {
        return value;
    }
}
