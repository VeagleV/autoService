package org.application.autoService.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ServicePartDto {
    private Long servicePartId;

    @NotNull(message = "serviceId не может быть null")
    private Long serviceId;

    @NotNull(message = "partId не может быть null")
    private Long partId;

    // геттеры и сеттеры
}
