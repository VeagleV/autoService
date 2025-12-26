package org.application.autoService.repository;

import org.application.autoService.dto.ClientTotalDto;
import org.application.autoService.dto.OrderStatisticsDto;
import org.application.autoService.model.Order;
import org.application.autoService.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClient(Client client);
    
    /**
     * Статистика заказов по периоду
     */
    @Query("SELECT new org.application.autoService.dto.OrderStatisticsDto(" +
           "COUNT(o), COALESCE(SUM(o.totalSum), 0.0), COALESCE(AVG(o.totalSum), 0.0)) " +
           "FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    OrderStatisticsDto getOrderStatistics(@Param("startDate") LocalDate startDate, 
                                         @Param("endDate") LocalDate endDate);
    
    /**
     * Общая стоимость заказов клиента
     */
    @Query("SELECT new org.application.autoService.dto.ClientTotalDto(" +
           "c.clientId, c.name, c.surname, COALESCE(SUM(o.totalSum), 0.0)) " +
           "FROM Client c LEFT JOIN Order o ON c.clientId = o.client.clientId " +
           "WHERE c.clientId = :clientId " +
           "GROUP BY c.clientId, c.name, c.surname")
    ClientTotalDto getClientTotalOrders(@Param("clientId") Long clientId);
    
    /**
     * Неоплаченные заказы с суммой выше указанной
     */
    @Query("SELECT o FROM Order o WHERE o.paymentStatus = 'NOT_PAID' AND o.totalSum >= :minSum ORDER BY o.totalSum DESC")
    List<Order> findUnpaidOrdersAboveSum(@Param("minSum") Double minSum);
}
