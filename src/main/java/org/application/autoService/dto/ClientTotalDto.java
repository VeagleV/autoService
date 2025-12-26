package org.application.autoService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для общей стоимости заказов клиента
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientTotalDto {
    
    /**
     * ID клиента
     */
    private Long clientId;
    
    /**
     * Имя клиента
     */
    private String name;
    
    /**
     * Фамилия клиента
     */
    private String surname;
    
    /**
     * Общая сумма всех заказов клиента
     */
    private Double totalSum;
}
