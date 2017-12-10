package net.pkhapps.dart.modules.dispatch.integration.mappers;

import net.pkhapps.dart.modules.dispatch.domain.Ticket;
import net.pkhapps.dart.modules.dispatch.domain.TicketType;
import net.pkhapps.dart.modules.dispatch.messages.v1.dto.TicketDto;
import net.pkhapps.dart.modules.dispatch.messages.v1.dto.TicketTypeDto;
import org.springframework.stereotype.Service;

/**
 * Mapper for converting a {@link Ticket} to a {@link TicketDto}.
 */
@Service
class TicketToDtoMapper implements ToDtoMapper<Ticket, TicketDto> {

    private final ToDtoMapper<TicketType, TicketTypeDto> ticketTypeToDtoMapper;

    TicketToDtoMapper(
            ToDtoMapper<TicketType, TicketTypeDto> ticketTypeToDtoMapper) {
        this.ticketTypeToDtoMapper = ticketTypeToDtoMapper;
    }

    @Override
    public TicketDto toDto(Ticket entity) {
        if (entity == null) {
            return null;
        }
        // TODO Implement me!
        TicketDto dto = new TicketDto();
        dto.type = ticketTypeToDtoMapper.toDto(entity.getType());
        return dto;
    }
}
