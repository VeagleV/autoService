package org.application.autoService.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRequestDto {
    private Long requestId;

    @NotNull(message = "ID пользователя не должен быть пустым")
    private Long userId;

    @NotBlank(message = "Заголовок не должен быть пустым")
    @Size(max = 200, message = "Заголовок не должен превышать 200 символов")
    private String title;

    @NotBlank(message = "Описание не должно быть пустым")
    private String description;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String priority;

    private String category;
}
