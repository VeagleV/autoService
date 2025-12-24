package org.application.autoService.mapper;

import org.application.autoService.dto.*;
import org.application.autoService.model.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(source = "client.clientId", target = "clientId")
    CarDto toDto(Car car);

    @Mapping(source = "clientId", target = "client.clientId")
    Car toEntity(CarDto dto);
}
