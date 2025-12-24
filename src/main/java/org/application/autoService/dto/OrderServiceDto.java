package org.application.autoService.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderServiceDto {
    private Long orderServiceId;

    @NotNull(message = "orderId не может быть null")
    private Long orderId;

    @NotNull(message = "serviceId не может быть null")
    private Long serviceId;

    // геттеры и сеттеры
}
