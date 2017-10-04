package net.pkhapps.dart.modules.dispatch.domain.base;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.Optional;

/**
 * TODO Document me
 */
public interface Auditable extends Persistable<ObjectId> {

    @NotNull Optional<Instant> getCreated();

    @NotNull Optional<String> getCreatedByUserId();

    @NotNull Optional<Instant> getLastModified();

    @NotNull Optional<String> getLastModifiedByUserId();
}
