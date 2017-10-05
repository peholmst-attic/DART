package net.pkhapps.dart.modules.dispatch.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository of {@link TicketType}s.
 */
public interface TicketTypeRepository extends MongoRepository<TicketType, ObjectId> {

    Optional<TicketType> findByCodeAndActiveTrue(String code);
}
