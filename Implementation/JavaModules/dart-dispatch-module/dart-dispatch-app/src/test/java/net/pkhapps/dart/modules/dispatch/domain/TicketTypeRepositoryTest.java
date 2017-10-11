package net.pkhapps.dart.modules.dispatch.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link TicketTypeRepository}.
 */
@SuppressWarnings("ConstantConditions")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TicketTypeRepositoryTest {

    @Autowired
    TicketTypeRepository ticketTypeRepository;

    @Test
    public void saveAndFindByCode() {
        TicketType ticketType = new TicketType("401");
        ticketType.setDescription("rakennuspalo: pieni", "byggnadsbrand: liten");
        ticketType = ticketTypeRepository.save(ticketType);

        Optional<TicketType> result = ticketTypeRepository.findByCodeAndActiveTrue("401");
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(ticketType);
    }

    @Test
    public void saveDeactivateAndFindByCode() {
        TicketType ticketType = new TicketType("402");
        ticketType.setDescription("rakennuspalo: keskisuuri", "byggnadsbrand: medelstor");
        ticketType.deactivate();
        ticketTypeRepository.save(ticketType);

        Optional<TicketType> result = ticketTypeRepository.findByCodeAndActiveTrue("402");
        assertThat(result.isPresent()).isFalse();
    }
}
