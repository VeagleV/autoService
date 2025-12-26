package org.application.autoService.service;

import org.application.autoService.dto.ClientTotalDto;
import org.application.autoService.dto.OrderStatisticsDto;
import org.application.autoService.model.Client;
import org.application.autoService.model.Order;
import org.application.autoService.model.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для выполнения специальных запросов к базе данных
 */
public interface QueryService {
    
    /**
     * Поиск клиентов по части email
     * 
     * @param emailPattern часть email для поиска
     * @return список найденных клиентов
     */
    List<Client> findClientsByEmail(String emailPattern);
    
    /**
     * Получить статистику заказов по периоду
     * 
     * @param startDate дата начала периода
     * @param endDate дата окончания периода
     * @return статистика заказов
     */
    OrderStatisticsDto getOrderStatistics(LocalDate startDate, LocalDate endDate);
    
    /**
     * Получить топ самых дорогих услуг
     * 
     * @param limit количество услуг
     * @return список услуг
     */
    List<Service> getTopExpensiveServices(int limit);
    
    /**
     * Получить общую стоимость заказов клиента
     * 
     * @param clientId ID клиента
     * @return информация о клиенте и общей сумме заказов
     */
    ClientTotalDto getClientTotalOrders(Long clientId);
    
    /**
     * Найти неоплаченные заказы с суммой выше указанной
     * 
     * @param minSum минимальная сумма заказа
     * @return список заказов
     */
    List<Order> findUnpaidOrdersAboveSum(Double minSum);
}
