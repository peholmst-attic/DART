package net.pkhapps.dart.map.jooq;

import net.pkhapps.dart.map.api.NameMatch;
import org.jetbrains.annotations.NotNull;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Document me!
 */
final class NameMatchUtils {

    private NameMatchUtils() {
    }

    /**
     * @param name
     * @param match
     * @param fields
     * @return
     */
    @NotNull
    public static Condition getNameMatchCondition(@NotNull String name, @NotNull NameMatch match, @NotNull Field<String>... fields) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(fields);
        Objects.requireNonNull(match);
        switch (match) {
            case CONTAINS:
                return DSL.or(Arrays.stream(fields).map(f -> f.likeIgnoreCase("%" + name + "%")).collect(Collectors.toList()));
            case ENDS_WITH:
                return DSL.or(Arrays.stream(fields).map(f -> f.likeIgnoreCase("%" + name)).collect(Collectors.toList()));
            case EXACT:
                return DSL.or(Arrays.stream(fields).map(f -> f.equalIgnoreCase(name)).collect(Collectors.toList()));
            case STARTS_WITH:
                return DSL.or(Arrays.stream(fields).map(f -> f.likeIgnoreCase(name + "%")).collect(Collectors.toList()));
        }
        throw new UnsupportedOperationException("Unsupported match: " + match);
    }
}
