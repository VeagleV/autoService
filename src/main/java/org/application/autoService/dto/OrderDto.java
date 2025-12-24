package org.application.autoService.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderDto {
    private Long orderId;

    @NotNull(message = "Дата заказа не может быть null")
    private LocalDate orderDate;

    @NotNull(message = "Сумма заказа не может быть null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Сумма должна быть положительной")
    private Double totalSum;

    @NotNull(message = "Статус оплаты не может быть null")
    private String paymentStatus; // либо enum String

    @NotNull(message = "clientId не может быть null")
    private Long clientId;

    @NotNull(message = "carId не может быть null")
    private Long carId;

    // геттеры и сеттеры
}
