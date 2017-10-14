package net.pkhapps.dart.modules.dispatch.domain.base;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.Optional;

/**
 * Interface to be implemented by persistable objects that include information about when they were created and
 * modified and by whom.
 */
public interface Auditable extends Persistable<ObjectId> {

    /**
     * Returns the instant when the persistable object was created.
     */
    @NotNull Optional<Instant> getCreated();

    /**
     * Returns the ID of the user that created the persistable object.
     */
    @NotNull Optional<String> getCreatedByUserId();

    /**
     * Returns the instant when the persistable object was last modified. If the object has not been modified, this
     * can either be an empty {@code Optional} or the same instant when it was created.
     */
    @NotNull Optional<Instant> getLastModified();

    /**
     * Returns the ID of the user that last modified the persistable object. If the object has not been modified,
     * this can either be an empty {@code Optional} or the user ID that created the object.
     */
    @NotNull Optional<String> getLastModifiedByUserId();
}
