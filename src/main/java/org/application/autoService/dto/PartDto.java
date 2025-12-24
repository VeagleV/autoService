package org.application.autoService.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PartDto {
    private Long partId;

    @NotBlank(message = "Название детали не должно быть пустым")
    private String name;

    @NotNull(message = "Цена детали не может быть null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть положительной")
    private Double price;

    // геттеры и сеттеры
}
