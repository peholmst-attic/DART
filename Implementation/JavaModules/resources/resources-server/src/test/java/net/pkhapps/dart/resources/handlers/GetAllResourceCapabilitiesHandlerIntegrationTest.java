package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.database.DSLContextFactory;
import net.pkhapps.dart.resources.messages.AllResourceCapabilities;
import net.pkhapps.dart.resources.messages.GetAllResourceCapabilities;
import net.pkhapps.dart.resources.queries.CapabilityQuery;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GetAllResourceCapabilitiesHandlerIntegrationTest {

    private GetAllResourceCapabilitiesHandler handler;

    @Before
    public void setUp() {
        handler = new GetAllResourceCapabilitiesHandler(DSLContextFactory.getInstance(), new CapabilityQuery(dataSourceProperties), Clock.systemDefaultZone());
    }

    @Test
    public void handleRequest_getAllCapabilities_allCapabilitiesAreReturned() {
        AllResourceCapabilities response = handler.handleRequest(new GetAllResourceCapabilities());
        assertTrue(response.getTimestamp().isPresent());
        assertFalse(response.getConversationId().isPresent());
        assertTrue(response.getCapabilities().size() > 0);
        response.getCapabilities().forEach(capability -> {
            assertNotNull(capability.getCapability());
            assertNotNull(capability.getDescription());
        });
    }
}
