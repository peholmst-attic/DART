package net.pkhapps.dart.modules.dispatch.domain;


import net.pkhapps.dart.modules.dispatch.domain.gis.Municipality;
import net.pkhapps.dart.modules.dispatch.domain.gis.StreetOrAddressPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link TicketRepository}.
 */
@SuppressWarnings("ConstantConditions")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TicketRepositoryTest {

    @Autowired
    TicketTypeRepository ticketTypeRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void openAndCloseWithSavesInBetween() {
        Ticket ticket = Ticket.open();
        ticket = ticketRepository.save(ticket);
        assertThat(ticket.getState()).isEqualTo(TicketState.NEW);

        ticket.close();
        ticket = ticketRepository.save(ticket);
        assertThat(ticket.getState()).isEqualTo(TicketState.CLOSED);
    }

    @Test
    public void openAndNarrowDownAddressAndAddDetailsWithSavesInBetween() {
        Municipality municipality = new Municipality("123", "Kunta", "Kommun");
        StreetOrAddressPoint street = new StreetOrAddressPoint("456", "Katu", "Gata", municipality);

        Ticket ticket = Ticket.open();
        ticket.setAddress(new FreeFormAddress(new Coordinates(0.0, 0.0), CoordinateSource.DISPATCHER,
                CoordinateAccuracy.MUNICIPALITY, "no exact addres yet", municipality));
        ticket = ticketRepository.save(ticket);

        ticket.setAddress(new StreetAddress(new Coordinates(1.0, 1.0), CoordinateSource.GIS,
                CoordinateAccuracy.ACCURATE, street, "5"));
        ticket = ticketRepository.save(ticket);

        TicketType ticketType = new TicketType("202");
        ticketType.setDescription("tieliikenneonnettomuus: pieni", "vägtrafikolycka: liten");
        ticketType = ticketTypeRepository.save(ticketType);

        ticket.setType(ticketType);
        ticket.setUrgency(TicketUrgency.A);
        ticket.setReporter("Joe Cool");
        ticket.setReporterPhone("123-4567890");
        ticket = ticketRepository.save(ticket);

        ticket.setDetails(
                "car crashed into a tree, driver ejected through the windshield, unconscious but breathing, no other " +
                "casualties");
        ticket = ticketRepository.save(ticket);

        ticket.close();
        ticketRepository.save(ticket);

        // Print the document just to check what it looks like
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(ticket.getId()));
        mongoTemplate.executeQuery(query, "tickets", System.out::println);
    }
}
