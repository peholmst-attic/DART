using DART.Base.Domain.Attributes;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace DART.Base.Domain.Test
{
    [TestClass]
    public class EntityTest
    {
        [TestMethod]
        public void NewEntityGetsUniqueLocalIdAndAggregateRootNextFreeLocalIdIsIncremented()
        {
            TestAggregate aggregate = new TestAggregate();
            TestEntity entity = new TestEntity(aggregate);

            Assert.AreEqual(1, entity.Id);
            Assert.AreEqual(2, aggregate.NextFreeLocalId);
        }

        [TestMethod]
        public void CopiedEntityHasSameIdAsOriginalWhileAggregateRootNextFreeLocalIdRemainsUntouched()
        {
            TestAggregate aggregate = new TestAggregate();
            TestEntity entity = new TestEntity(aggregate);

            var oldNextFreeLocalId = aggregate.NextFreeLocalId;

            TestEntity copy = new TestEntity(entity);

            Assert.AreEqual(entity.Id, copy.Id);
            Assert.AreEqual(oldNextFreeLocalId, aggregate.NextFreeLocalId);
        }

        [TestMethod]
        public void EntitiesWithSameIdAreConsideredEqual()
        {
            TestAggregate aggregate = new TestAggregate();
            TestEntity entity = new TestEntity(aggregate);
            TestEntity copy = new TestEntity(entity);

            Assert.AreEqual(entity, copy);
            Assert.AreNotSame(entity, copy);
        }
     
    }

    [AggregateName("test-documents")]
    public class TestAggregate : AggregateRoot
    {
    }

    public class TestEntity : Entity
    {
        public TestEntity(TestAggregate aggregateRoot) : base(aggregateRoot)
        {
        }

        public TestEntity(TestEntity original) : base(original)
        {
        }
    }
}
