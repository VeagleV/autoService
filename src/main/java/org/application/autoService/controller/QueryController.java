package org.application.autoService.controller;

import org.application.autoService.dto.ClientDto;
import org.application.autoService.dto.ClientTotalDto;
import org.application.autoService.dto.OrderDto;
import org.application.autoService.dto.OrderStatisticsDto;
import org.application.autoService.dto.ServiceDto;
import org.application.autoService.mapper.ClientMapper;
import org.application.autoService.mapper.OrderMapper;
import org.application.autoService.mapper.ServiceMapper;
import org.application.autoService.model.Client;
import org.application.autoService.model.Order;
import org.application.autoService.model.Service;
import org.application.autoService.service.QueryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для выполнения специальных запросов к базе данных
 */
@RestController
@RequestMapping("/api/queries")
@CrossOrigin
public class QueryController {
    
    private final QueryService queryService;
    private final ClientMapper clientMapper;
    private final OrderMapper orderMapper;
    private final ServiceMapper serviceMapper;
    
    public QueryController(QueryService queryService,
                          ClientMapper clientMapper,
                          OrderMapper orderMapper,
                          ServiceMapper serviceMapper) {
        this.queryService = queryService;
        this.clientMapper = clientMapper;
        this.orderMapper = orderMapper;
        this.serviceMapper = serviceMapper;
    }
    
    /**
     * Запрос 1: Поиск клиентов по email
     * SQL: SELECT * FROM client WHERE email LIKE %параметр%
     * 
     * @param email часть email для поиска
     * @return список клиентов
     */
    @GetMapping("/clients-by-email")
    public ResponseEntity<List<ClientDto>> findClientsByEmail(@RequestParam String email) {
        List<Client> clients = queryService.findClientsByEmail(email);
        List<ClientDto> clientDtos = clients.stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientDtos);
    }
    
    /**
     * Запрос 2: Статистика заказов по периоду
     * SQL: SELECT COUNT(*), SUM(total_sum), AVG(total_sum) FROM "order" WHERE order_date BETWEEN дата1 AND дата2
     * 
     * @param startDate дата начала периода
     * @param endDate дата окончания периода
     * @return статистика заказов
     */
    @GetMapping("/orders-statistics")
    public ResponseEntity<OrderStatisticsDto> getOrderStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        OrderStatisticsDto statistics = queryService.getOrderStatistics(startDate, endDate);
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * Запрос 3: Топ самых дорогих услуг
     * SQL: SELECT * FROM service ORDER BY price DESC LIMIT N
     * 
     * @param limit количество топ-N услуг
     * @return список услуг
     */
    @GetMapping("/top-services")
    public ResponseEntity<List<ServiceDto>> getTopExpensiveServices(@RequestParam int limit) {
        List<Service> services = queryService.getTopExpensiveServices(limit);
        List<ServiceDto> serviceDtos = services.stream()
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(serviceDtos);
    }
    
    /**
     * Запрос 4: Общая стоимость заказов клиента
     * SQL: SELECT c.name, c.surname, SUM(o.total_sum) FROM client c JOIN "order" o ON c.client_id = o.client_id WHERE c.client_id = параметр GROUP BY c.client_id
     * 
     * @param clientId ID клиента
     * @return информация о клиенте и общей сумме заказов
     */
    @GetMapping("/client-total")
    public ResponseEntity<ClientTotalDto> getClientTotalOrders(@RequestParam Long clientId) {
        ClientTotalDto clientTotal = queryService.getClientTotalOrders(clientId);
        return ResponseEntity.ok(clientTotal);
    }
    
    /**
     * Запрос 5: Неоплаченные заказы с суммой выше указанной
     * SQL: SELECT * FROM "order" WHERE payment_status = 'NOT_PAID' AND total_sum >= параметр ORDER BY total_sum DESC
     * 
     * @param minSum минимальная сумма заказа
     * @return список заказов
     */
    @GetMapping("/unpaid-orders")
    public ResponseEntity<List<OrderDto>> findUnpaidOrdersAboveSum(@RequestParam Double minSum) {
        List<Order> orders = queryService.findUnpaidOrdersAboveSum(minSum);
        List<OrderDto> orderDtos = orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }
}
