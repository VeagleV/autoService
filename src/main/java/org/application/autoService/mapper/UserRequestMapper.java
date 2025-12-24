package org.application.autoService.mapper;

import org.application.autoService.dto.UserRequestDto;
import org.application.autoService.model.UserRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {
    
    @Mapping(target = "status", source = "status")
    UserRequestDto toDto(UserRequest userRequest);
    
    @Mapping(target = "status", source = "status")
    UserRequest toEntity(UserRequestDto dto);
    
    default String map(UserRequest.RequestStatus status) {
        return status != null ? status.name() : null;
    }
    
    default UserRequest.RequestStatus map(String status) {
        if (status == null || status.isEmpty()) {
            return UserRequest.RequestStatus.NEW;
        }
        try {
            return UserRequest.RequestStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UserRequest.RequestStatus.NEW;
        }
    }
}
