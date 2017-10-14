package net.pkhapps.dart.modules.dispatch.domain;

import org.junit.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Ticket}.
 */
@SuppressWarnings("ConstantConditions")
public class TicketTest {

    @Test
    public void assignResource_notPreviouslyAssigned_resourceIsAssigned() {
        Ticket ticket = Ticket.open();
        Instant instant = Instant.now();
        ticket.assignResource("PG31", instant);

        Optional<TicketResource> pg31 = ticket.findLatestResource("PG31");
        assertThat(pg31.isPresent()).isTrue();
        assertThat(pg31.get().getAssigned()).isEqualTo(instant);
    }

    @Test
    public void assignResource_alreadyAssigned_nothingHappens() {
        Ticket ticket = Ticket.open();
        Instant instant = Instant.now();
        ticket.assignResource("PG31", instant);
        ticket.assignResource("PG31", instant.plusSeconds(10));

        Optional<TicketResource> pg31 = ticket.findLatestResource("PG31");
        assertThat(pg31.isPresent()).isTrue();
        assertThat(pg31.get().getAssigned()).isEqualTo(instant);
        assertThat(ticket.findResources("PG31")).hasSize(1);
    }

    @Test
    public void assignResource_wasAssignedButNotAnymore_resourceIsAssignedAgain() {
        Ticket ticket = Ticket.open();
        Instant firstInstant = Instant.now();
        ticket.assignResource("PG31", firstInstant);
        ticket.recordResourceEvent("PG31", TicketResourceEventType.AVAILABLE, firstInstant.plusSeconds(10));

        Instant secondInstant = firstInstant.plusSeconds(20);
        ticket.assignResource("PG31", secondInstant);

        Optional<TicketResource> pg31 = ticket.findLatestResource("PG31");
        assertThat(pg31.isPresent()).isTrue();
        assertThat(pg31.get().getAssigned()).isEqualTo(secondInstant);
        assertThat(ticket.findResources("PG31")).hasSize(2);
    }

    @Test
    public void recordResourceEvent_goThroughEntireChainOfEventsInCorrectOrder_eventsAreRecorded() {
        Ticket ticket = Ticket.open();
        Instant instant = Instant.now();
        ticket.assignResource("PG31", instant);
        ticket.recordResourceEvent("PG31", TicketResourceEventType.DISPATCHED, instant.plusSeconds(10));
        ticket.recordResourceEvent("PG31", TicketResourceEventType.EN_ROUTE, instant.plusSeconds(20));
        ticket.recordResourceEvent("PG31", TicketResourceEventType.ON_SCENE, instant.plusSeconds(30));
        ticket.recordResourceEvent("PG31", TicketResourceEventType.AVAILABLE, instant.plusSeconds(40));
        ticket.recordResourceEvent("PG31", TicketResourceEventType.RETURNED, instant.plusSeconds(50));

        Optional<TicketResource> pg31 = ticket.findLatestResource("PG31");
        assertThat(pg31.isPresent()).isTrue();
        assertThat(pg31.get().getAssigned()).isEqualTo(instant);
        assertThat(pg31.get().getDispatched()).isEqualTo(instant.plusSeconds(10));
        assertThat(pg31.get().getEnRoute()).isEqualTo(instant.plusSeconds(20));
        assertThat(pg31.get().getOnScene()).isEqualTo(instant.plusSeconds(30));
        assertThat(pg31.get().getAvailable()).isEqualTo(instant.plusSeconds(40));
        assertThat(pg31.get().getReturned()).isEqualTo(instant.plusSeconds(50));
        assertThat(pg31.get().isAssignedToTicket()).isFalse();
    }

    @Test
    public void recordResourceEvent_firstDispatchedResource_ticketStateIsChangedToDispatched() {
        Ticket ticket = Ticket.open();
        assertThat(ticket.getState()).isEqualTo(TicketState.NEW);

        ticket.assignResource("PG31", Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.DISPATCHED, Instant.now());
        assertThat(ticket.getState()).isEqualTo(TicketState.DISPATCHED);
    }

    @Test
    public void recordResourceEvent_secondDispatchedResource_ticketStateRemainsDispatched() {
        Ticket ticket = Ticket.open();

        ticket.assignResource("PG31", Instant.now());
        ticket.assignResource("PG21", Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.DISPATCHED, Instant.now());
        ticket.recordResourceEvent("PG21", TicketResourceEventType.DISPATCHED, Instant.now());
        assertThat(ticket.getState()).isEqualTo(TicketState.DISPATCHED);
    }

    @Test
    public void recordResourceEvent_oneResourceOfManyDetaches_ticketStateRemainsDispatched() {
        Ticket ticket = Ticket.open();

        ticket.assignResource("PG31", Instant.now());
        ticket.assignResource("PG21", Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.DISPATCHED, Instant.now());
        ticket.recordResourceEvent("PG21", TicketResourceEventType.DISPATCHED, Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.AVAILABLE, Instant.now());
        assertThat(ticket.getState()).isEqualTo(TicketState.DISPATCHED);
    }

    @Test
    public void recordResourceEvent_allResourcesDetachWithoutReachingTheScene_ticketStateIsChangedToOnHold() {
        Ticket ticket = Ticket.open();

        ticket.assignResource("PG31", Instant.now());
        ticket.assignResource("PG21", Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.DISPATCHED, Instant.now());
        ticket.recordResourceEvent("PG21", TicketResourceEventType.DISPATCHED, Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.AVAILABLE, Instant.now());
        ticket.recordResourceEvent("PG21", TicketResourceEventType.RETURNED, Instant.now());
        assertThat(ticket.getState()).isEqualTo(TicketState.ON_HOLD);
    }

    @Test
    public void
    recordResourceEvent_allResourcesDetachAfterAtLeastOneReachedTheScene_ticketStateIsChangedToUnderObservation() {
        Ticket ticket = Ticket.open();

        ticket.assignResource("PG31", Instant.now());
        ticket.assignResource("PG21", Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.DISPATCHED, Instant.now());
        ticket.recordResourceEvent("PG21", TicketResourceEventType.DISPATCHED, Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.ON_SCENE, Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.AVAILABLE, Instant.now());
        ticket.recordResourceEvent("PG21", TicketResourceEventType.RETURNED, Instant.now());
        assertThat(ticket.getState()).isEqualTo(TicketState.UNDER_OBSERVATION);
    }

    @Test
    public void recordResourceEvent_resourceIsReattachedToTicketUnderObservation_ticketStateChangesToDispatched() {
        Ticket ticket = Ticket.open();
        ticket.assignResource("PG31", Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.DISPATCHED, Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.ON_SCENE, Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.AVAILABLE, Instant.now());
        assertThat(ticket.getState()).isEqualTo(TicketState.UNDER_OBSERVATION);
        ticket.assignResource("PG31", Instant.now());
        ticket.recordResourceEvent("PG31", TicketResourceEventType.DISPATCHED, Instant.now());
        assertThat(ticket.getState()).isEqualTo(TicketState.DISPATCHED);
    }

    @Test
    public void recordResourceEvent_resourceIsAttachedToTicketOnHold_ticketStateChangesToDispatched() {
        Ticket ticket = Ticket.open();
        ticket.putOnHold();
        ticket.assignResource("PG31", Instant.now());
        assertThat(ticket.getState()).isEqualTo(TicketState.ON_HOLD);
        ticket.recordResourceEvent("PG31", TicketResourceEventType.DISPATCHED, Instant.now());
        assertThat(ticket.getState()).isEqualTo(TicketState.DISPATCHED);
    }

    @Test
    public void putOnHold_resourceIsNew_stateIsChanged() {
        Ticket ticket = Ticket.open();
        assertThat(ticket.getState()).isEqualTo(TicketState.NEW);

        ticket.putOnHold();
        assertThat(ticket.getState()).isEqualTo(TicketState.ON_HOLD);
    }
}
