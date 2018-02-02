using System;
using MongoDB.Driver;
using MongoDB.Bson;

namespace DART.Base.Domain
{
    /// <summary>
    /// Repository implementation that uses MongoDB to store aggregates.
    /// </summary>
    /// <typeparam name="T">The aggregate root type.</typeparam>
    public class MongoRepository<T> : IRepository<T> where T : AggregateRoot
    {
        private readonly IMongoCollection<T> mongoCollection;

        /// <summary>
        /// Creates a new MongoRepository, using the aggregate name of the aggregate root as the collection name.
        /// </summary>
        /// <param name="mongoDatabase">The MongoDatabase to work with.</param>
        /// <seealso cref="AggregateNameUtil"/>
        public MongoRepository(IMongoDatabase mongoDatabase) : this(mongoDatabase, AggregateNameUtil.GetAggregateName<T>())
        {
        }

        /// <summary>
        /// Creates a new MongoRepository.
        /// </summary>
        /// <param name="mongoDatabase">The MongoDatabase to work with.</param>
        /// <param name="collectionName">The name of the collection that contains the aggregates.</param>
        public MongoRepository(IMongoDatabase mongoDatabase, string collectionName)
        {
            mongoCollection = mongoDatabase.GetCollection<T>(collectionName);
        }

        public T Find(ObjectId id)
        {            
            throw new NotImplementedException();
        }

        public void Save(T aggregate)
        {
            throw new NotImplementedException();
        }

        public void Delete(ObjectId id)
        {
            throw new NotImplementedException();
        }
    }
}
