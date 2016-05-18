package net.pkhapps.dart.common;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class CollectionsUtil {

    private CollectionsUtil() {
    }

    @NotNull
    public static <T> Set<T> unmodifiableCopy(@NotNull Set<T> original) {
        return Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(original, "the original set must not be null")));
    }
}
