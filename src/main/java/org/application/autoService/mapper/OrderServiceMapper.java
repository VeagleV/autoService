package org.application.autoService.mapper;

import org.application.autoService.dto.*;
import org.application.autoService.model.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderServiceMapper {

    @Mapping(source = "order.orderId", target = "orderId")
    @Mapping(source = "service.serviceId", target = "serviceId")
    OrderServiceDto toDto(OrderService orderService);

    @Mapping(source = "orderId", target = "order.orderId")
    @Mapping(source = "serviceId", target = "service.serviceId")
    OrderService toEntity(OrderServiceDto dto);
}
