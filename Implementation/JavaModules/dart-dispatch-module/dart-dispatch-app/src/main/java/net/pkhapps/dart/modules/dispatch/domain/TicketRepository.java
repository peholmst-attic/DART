package net.pkhapps.dart.modules.dispatch.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository of {@link Ticket}s.
 */
public interface TicketRepository extends MongoRepository<Ticket, ObjectId> {
}
