package net.pkhapps.dart.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class CollectionsUtil {

    private CollectionsUtil() {
    }

    public static <T> Set<T> unmodifiableCopy(Set<T> original) {
        if (original == null) {
            return null;
        } else {
            return Collections.unmodifiableSet(new HashSet<>(original));
        }
    }
}
