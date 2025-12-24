package org.application.autoService.mapper;

import org.application.autoService.dto.UserRequestDto;
import org.application.autoService.model.UserRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {
    
    @Mapping(target = "status", expression = "java(userRequest.getStatus() != null ? userRequest.getStatus().name() : null)")
    UserRequestDto toDto(UserRequest userRequest);
    
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? org.application.autoService.model.UserRequest.RequestStatus.valueOf(dto.getStatus()) : org.application.autoService.model.UserRequest.RequestStatus.NEW)")
    UserRequest toEntity(UserRequestDto dto);
}
