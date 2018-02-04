using System;
using System.Collections.Generic;
using System.Text;

namespace DART.Base.Domain
{
    /// <summary>
    /// TODO Document me
    /// </summary>
    public interface IDomainContext
    {
        /// <summary>
        /// The current date and time.
        /// </summary>
        DateTime CurrentDateTime { get; }

        // TODO Current user
    }
}
