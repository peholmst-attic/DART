package net.pkhapps.dart.modules.dispatch.integration.handlers;

import net.pkhapps.dart.modules.dispatch.domain.Ticket;
import net.pkhapps.dart.modules.dispatch.domain.TicketRepository;
import net.pkhapps.dart.modules.dispatch.integration.mappers.ToDtoMapper;
import net.pkhapps.dart.modules.dispatch.messages.v1.command.OpenTicketCommand;
import net.pkhapps.dart.modules.dispatch.messages.v1.dto.TicketDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handler for {@link OpenTicketCommand}.
 */
@Service
class OpenTicketCommandHandler {

    private final TicketRepository ticketRepository;
    private final ToDtoMapper<Ticket, TicketDto> ticketToDtoMapper;

    OpenTicketCommandHandler(TicketRepository ticketRepository,
                             ToDtoMapper<Ticket, TicketDto> ticketToDtoMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketToDtoMapper = ticketToDtoMapper;
    }

    @ServiceActivator
    @Transactional
    @NotNull TicketDto handle(@NotNull OpenTicketCommand command) {
        return ticketToDtoMapper.toDto(ticketRepository.save(Ticket.open()));
    }
}
