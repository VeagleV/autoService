package org.application.autoService.mapper;

import org.application.autoService.dto.*;
import org.application.autoService.model.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDto toDto(Client client);
    Client toEntity(ClientDto dto);
}
