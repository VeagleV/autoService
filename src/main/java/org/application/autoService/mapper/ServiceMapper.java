package org.application.autoService.mapper;

import org.application.autoService.dto.*;
import org.application.autoService.model.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceDto toDto(Service service);
    Service toEntity(ServiceDto dto);
}
