package net.pkhapps.dart.base.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base interface for repositories of {@link AbstractAggregateRoot aggregate roots}.
 */
@NoRepositoryBean
public interface Repository<T extends AbstractAggregateRoot> extends MongoRepository<T, ObjectId> {
}
