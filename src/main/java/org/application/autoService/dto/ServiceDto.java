package org.application.autoService.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ServiceDto {
    private Long serviceId;

    @NotBlank(message = "Название услуги не должно быть пустым")
    private String name;

    @NotNull(message = "Цена услуги не может быть null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть положительной")
    private Double price;

    // геттеры и сеттеры
}
