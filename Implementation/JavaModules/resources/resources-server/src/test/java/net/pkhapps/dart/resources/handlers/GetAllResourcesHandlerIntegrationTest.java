package net.pkhapps.dart.resources.handlers;

import net.pkhapps.dart.database.DSLContextFactory;
import net.pkhapps.dart.database.DataSourceProperties;
import net.pkhapps.dart.resources.messages.AllResources;
import net.pkhapps.dart.resources.messages.GetAllResources;
import net.pkhapps.dart.resources.queries.ResourceQuery;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for {@link GetAllResourcesHandler}.
 */
public class GetAllResourcesHandlerIntegrationTest {

    private GetAllResourcesHandler handler;

    @Before
    public void setUp() {
        handler = new GetAllResourcesHandler(DSLContextFactory.getInstance(),
                new ResourceQuery(DataSourceProperties.getInstance()), Clock.systemDefaultZone());
    }

    @Test
    public void handleRequest_getAllActiveResources_allActiveResourcesAreReturned() {
        AllResources response = handler.handleRequest(new GetAllResources(false));
        assertTrue(response.getTimestamp().isPresent());
        assertFalse(response.getConversationId().isPresent());
        assertTrue(response.getResources().size() > 0);
        response.getResources().forEach(resource -> {
            assertNotNull(resource.getResource());
            assertFalse(resource.isDisabled());
            assertTrue(resource.getCapabilities().size() > 0);
        });
    }
}
