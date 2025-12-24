package org.application.autoService.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CarDto {
    private Long carId;

    @NotBlank(message = "Марка не должна быть пустой")
    private String brand;

    @NotBlank(message = "Модель не должна быть пустой")
    private String model;

    @NotBlank(message = "Тип двигателя не должен быть пустым")
    private String engineType;

    @NotNull(message = "Год выпуска не может быть null")
    @Min(value = 1886, message = "Год выпуска должен быть >= 1886")
    private Integer year;

    @NotNull(message = "clientId не может быть null")
    private Long clientId;

    // геттеры и сеттеры
}
