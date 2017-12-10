package net.pkhapps.dart.modules.dispatch.domain.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be placed on domain events that should only be published once, regardless of how many times they
 * have been {@link AbstractAggregateRoot#registerEvent(Object) registered}. When this annotation is in place, a domain
 * event will be ignored if another event that is equal to the event in question has already been registered.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MergeEqualEvents {
}
