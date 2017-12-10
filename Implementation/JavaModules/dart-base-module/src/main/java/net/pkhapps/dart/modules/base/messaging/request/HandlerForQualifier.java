package net.pkhapps.dart.modules.base.messaging.request;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Qualifier class for the {@link HandlerFor} annotation, used for programmatic CDI lookups.
 */
class HandlerForQualifier extends AnnotationLiteral<HandlerFor> implements

        HandlerFor {

    private final Class<? extends Request> value;

    HandlerForQualifier(
            Class<? extends Request> value) {
        this.value = value;
    }

    @Override
    public Class<? extends Request> value() {
        return value;
    }
}
