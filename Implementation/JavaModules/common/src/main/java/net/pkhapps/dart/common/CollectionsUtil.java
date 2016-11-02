package net.pkhapps.dart.common;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility methods for working with collections.
 */
public final class CollectionsUtil {

    private CollectionsUtil() {
    }

    /**
     * Creates and returns an unmodifiable copy of the specified set.
     *
     * @param original the original set to copy.
     * @return an unmodifiable copy of the original set, or {@code null} if the original set was also {@code null}.
     */
    @Nullable
    public static <T> Set<T> unmodifiableCopy(@Nullable Set<T> original) {
        if (original == null) {
            return null;
        } else {
            return Collections.unmodifiableSet(new HashSet<>(original));
        }
    }
}
