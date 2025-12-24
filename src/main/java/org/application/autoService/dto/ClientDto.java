package org.application.autoService.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClientDto {
    private Long clientId;

    @NotBlank(message = "Фамилия не должна быть пустой")
    private String surname;

    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    private String middleName;

    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email не должен быть пустым")
    private String email;

    @NotBlank(message = "Телефон не должен быть пустым")
    private String phone;

    // геттеры и сеттеры
}
