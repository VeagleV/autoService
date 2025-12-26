package org.application.autoService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для статистики заказов по периоду
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsDto {
    
    /**
     * Количество заказов
     */
    private Long orderCount;
    
    /**
     * Общая сумма заказов
     */
    private Double totalSum;
    
    /**
     * Средняя сумма заказа
     */
    private Double averageSum;
}
