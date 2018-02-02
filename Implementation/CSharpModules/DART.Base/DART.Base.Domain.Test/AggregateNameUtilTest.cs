using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace DART.Base.Domain.Test
{
    [TestClass]
    public class AggregateNameUtilTest
    {
        [TestMethod]
        public void GetAggregateName_ClassWithAttributeAsInput_AggregateNameAsOutput()
        {
            var output = AggregateNameUtil.GetAggregateName<TestAggregate>();
            Assert.AreEqual("test-documents", output);
        }

        [TestMethod]
        public void GetAggregateName_ClassWithoutAttributeAsInput_ClassNameAsOutput()
        {
            var output = AggregateNameUtil.GetAggregateName<TestAggregateWithoutNameAttribute>();
            Assert.AreEqual("TestAggregateWithoutNameAttribute", output);
        }
    }

    public class TestAggregateWithoutNameAttribute : AggregateRoot
    {
    }
}
