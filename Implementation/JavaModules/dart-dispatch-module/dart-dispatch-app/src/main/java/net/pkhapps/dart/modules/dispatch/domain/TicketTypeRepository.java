package net.pkhapps.dart.modules.dispatch.domain;

import net.pkhapps.dart.base.domain.Repository;

import java.util.Optional;

/**
 * Repository of {@link TicketType}s.
 */
public interface TicketTypeRepository extends Repository<TicketType> {

    Optional<TicketType> findByCodeAndActiveTrue(String code);
}
