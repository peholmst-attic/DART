package net.pkhapps.dart.modules.dispatch.integration.mappers;

import net.pkhapps.dart.modules.dispatch.domain.TicketType;
import net.pkhapps.dart.modules.dispatch.messages.v1.dto.TicketTypeDto;
import org.springframework.stereotype.Service;

/**
 * Mapper for converting a {@link TicketType} to a {@link TicketTypeDto}.
 */
@Service
class TicketTypeToDtoMapper implements ToDtoMapper<TicketType, TicketTypeDto> {

    @Override
    public TicketTypeDto toDto(TicketType entity) {
        if (entity == null) {
            return null;
        }
        TicketTypeDto dto = new TicketTypeDto();
        dto.active = entity.isActive();
        dto.code = entity.getCode();
        dto.descriptionFi = entity.getDescriptionFi();
        dto.descriptionSv = entity.getDescriptionSv();
        return dto;
    }
}
