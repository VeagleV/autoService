package org.application.autoService.mapper;

import org.application.autoService.dto.*;
import org.application.autoService.model.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "client.clientId", target = "clientId")
    @Mapping(source = "car.carId", target = "carId")
    @Mapping(target = "paymentStatus", expression = "java(order.getPaymentStatus() != null ? order.getPaymentStatus().name() : null)")
    OrderDto toDto(Order order);

    @Mapping(source = "clientId", target = "client.clientId")
    @Mapping(source = "carId", target = "car.carId")
    @Mapping(target = "paymentStatus", expression = "java(orderDto.getPaymentStatus() != null ? org.application.autoService.model.Order.PaymentStatus.valueOf(orderDto.getPaymentStatus()) : null)")
    Order toEntity(OrderDto orderDto);
}
