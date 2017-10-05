package net.pkhapps.dart.modules.dispatch.domain;

import net.pkhapps.dart.modules.dispatch.domain.base.AbstractEventSourcedAggregateRoot;
import net.pkhapps.dart.modules.dispatch.domain.base.SoftDeletable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * Entity representing a ticket type.
 */
@Document
public class TicketType extends AbstractEventSourcedAggregateRoot implements SoftDeletable {

    @Indexed(unique = true)
    private String code;

    private String descriptionFi;

    private String descriptionSv;

    private boolean active;

    public static class Initialize extends AbstractAction<TicketType> {

        private final String code;

        @PersistenceConstructor
        Initialize(@NotNull String code) {
            this.code = Objects.requireNonNull(code, "code must not be null");
        }

        @Override
        protected void doApply(@NotNull TicketType aggregateRoot) {
            aggregateRoot.code = this.code;
            aggregateRoot.active = true;
        }
    }

    public static class Deactivate extends AbstractAction<TicketType> {

        @Override
        protected void doApply(@NotNull TicketType aggregateRoot) {
            aggregateRoot.active = false;
        }
    }

    public static class Activate extends AbstractAction<TicketType> {

        @Override
        protected void doApply(@NotNull TicketType aggregateRoot) {
            aggregateRoot.active = true;
        }
    }

    public static class UpdateDescription extends AbstractAction<TicketType> {

        private final String descriptionFi;
        private final String descriptionSv;

        @PersistenceConstructor
        UpdateDescription(String descriptionFi, String descriptionSv) {
            this.descriptionFi = descriptionFi;
            this.descriptionSv = descriptionSv;
        }

        @Override
        protected void doApply(@NotNull TicketType aggregateRoot) {
            aggregateRoot.descriptionFi = this.descriptionFi;
            aggregateRoot.descriptionSv = this.descriptionSv;
        }
    }

    /**
     * Used by Spring Data only.
     */
    @SuppressWarnings("unused")
    private TicketType() {
    }

    public TicketType(@NotNull String code) {
        performAction(new Initialize(code));
    }

    public TicketType(@NotNull TicketType original) {
        super(original);
    }

    public @NotNull String getCode() {
        return code;
    }

    public @Nullable String getDescriptionFi() {
        return descriptionFi;
    }

    public void setDescriptionFi(@Nullable String descriptionFi) {
        performAction(new UpdateDescription(descriptionFi, descriptionSv));
    }

    public @Nullable String getDescriptionSv() {
        return descriptionSv;
    }

    public void setDescriptionSv(@Nullable String descriptionSv) {
        performAction(new UpdateDescription(descriptionFi, descriptionSv));
    }

    public void setDescription(@Nullable String descriptionFi, @Nullable String descriptionSv) {
        performAction(new UpdateDescription(descriptionFi, descriptionSv));
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void activate() {
        if (!active) {
            performAction(new Activate());
        }
    }

    @Override
    public void deactivate() {
        if (active) {
            performAction(new Deactivate());
        }
    }
}
