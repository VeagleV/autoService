package org.application.autoService.mapper;

import org.application.autoService.dto.*;
import org.application.autoService.model.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ServicePartMapper {

    @Mapping(source = "service.serviceId", target = "serviceId")
    @Mapping(source = "part.partId", target = "partId")
    ServicePartDto toDto(ServicePart sp);

    @Mapping(source = "serviceId", target = "service.serviceId")
    @Mapping(source = "partId", target = "part.partId")
    ServicePart toEntity(ServicePartDto dto);
}
