using Microsoft.VisualStudio.TestTools.UnitTesting;
using MongoDB.Driver;

namespace DART.Base.Domain.Test
{
    [TestClass]
    public class MongoRepositoryIntegrationTest
    {
        private IRepository<TestAggregate> repository;

        [TestInitialize]
        public void SetUp()
        {
            // Make sure you have MongoDB running on the machine where the tests are running
            var connectionString = "mongodb://localhost:27017"; // TODO Make this configurable
            var client = new MongoClient(connectionString);
            var database = client.GetDatabase("test-database");
            // TODO Clear database
            repository = new MongoRepository<TestAggregate>(database);
        }

        [TestMethod]
        public void Save_NewAggregate_CopyIsReturnedAndOriginalIsUntouched()
        {
            var aggregate = CreateTestAggregate();
            var saved = repository.Save(aggregate);

            Assert.IsNull(aggregate.Id);
            Assert.IsNull(aggregate.Created);
            Assert.IsNull(aggregate.LastModified);
            Assert.IsNull(aggregate.Version);

            Assert.IsNotNull(saved.Id);
            Assert.IsNotNull(saved.Created);
            Assert.IsNotNull(saved.LastModified);
            Assert.AreEqual(1, saved.Version);

            Assert.IsNotNull(repository.FindById(saved.Id));
        }

        [TestMethod]
        public void Save_ExistingAggregate_CopyIsReturnedWithIncreasedVersion()
        {
            var aggregate = repository.Save(CreateTestAggregate());
            aggregate.StringProperty = "Updated Hello World";
            var saved = repository.Save(aggregate);
            var retrieved = repository.FindById(saved.Id);

            Assert.AreEqual(1, aggregate.Version);
           
            Assert.IsNotNull(saved.Id);
            Assert.IsNotNull(saved.Created);
            Assert.IsNotNull(saved.LastModified);
            Assert.AreEqual(2, saved.Version);

            Assert.AreEqual(saved.Version, retrieved.Version);
            Assert.AreEqual(saved.StringProperty, retrieved.StringProperty);
        }

        [TestMethod]
        [ExpectedException(typeof(OptimisticLockingFailureException))]
        public void Save_AggregateUpdatedByOtherClient_OptimisticLockingExceptionThrown()
        {
            var firstClient = repository.Save(CreateTestAggregate());

            var secondClient = repository.FindById(firstClient.Id);
            secondClient.StringProperty = "Updated Foo Bar";
            repository.Save(secondClient);

            firstClient.StringProperty = "Another Updated Foo Bar";
            repository.Save(firstClient);

            Assert.AreEqual(secondClient.Version, repository.FindById(secondClient.Id).Version);
        }

        [TestMethod]
        public void FindById_AggregateDoesNotExist_NullIsReturned()
        {
            var result = repository.FindById("nonexistent");
            Assert.IsNull(result);
        }

        [TestMethod]
        [ExpectedException(typeof(AggregateNotFoundException))]
        public void Delete_AggregateDoesNotExist_ExceptionIsThrown()
        {
            repository.Delete("nonexistent");
        }

        [TestMethod]
        public void Delete_AggregateExists_AggregateHasBeenRemoved()
        {
            var aggregate = repository.Save(CreateTestAggregate());
            Assert.IsNotNull(repository.FindById(aggregate.Id));
            repository.Delete(aggregate.Id);
            Assert.IsNull(repository.FindById(aggregate.Id));
        }

        private TestAggregate CreateTestAggregate()
        {
            return new TestAggregate
            {
                StringProperty = "Hello World",
                IntProperty = 123
            };
        }
    }
}
