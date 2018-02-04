using MongoDB.Driver;

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

        public T FindById(string id)
        {
            return mongoCollection.Find(aggregate => aggregate.Id == id).FirstOrDefault();
        }

        public T Save(T aggregate)
        {
            var copyToSave = (T) aggregate.CopyForSaving();
            if (copyToSave.Id == null) {
                mongoCollection.InsertOne(copyToSave);
            }
            else
            {
                var result = mongoCollection.ReplaceOne(a => (a.Id == aggregate.Id && a.Version == aggregate.Version), copyToSave);
                if (result.ModifiedCount == 0)
                {
                    throw new OptimisticLockingFailureException(aggregate.Id);
                }
            }
            return copyToSave;
        }

        public void Delete(string id)
        {
            var result = mongoCollection.DeleteOne(aggregate => aggregate.Id == id);
            if (result.DeletedCount == 0)
            {
                throw new AggregateNotFoundException(id);
            }
        }
    }
}
