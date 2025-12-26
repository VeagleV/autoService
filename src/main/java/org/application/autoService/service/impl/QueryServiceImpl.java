package org.application.autoService.service.impl;

import org.application.autoService.dto.ClientTotalDto;
import org.application.autoService.dto.OrderStatisticsDto;
import org.application.autoService.model.Client;
import org.application.autoService.model.Order;
import org.application.autoService.repository.ClientRepository;
import org.application.autoService.repository.OrderRepository;
import org.application.autoService.repository.ServiceRepository;
import org.application.autoService.service.QueryService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Реализация сервиса для выполнения специальных запросов к базе данных
 */
@org.springframework.stereotype.Service
@Transactional(readOnly = true)
public class QueryServiceImpl implements QueryService {
    
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final ServiceRepository serviceRepository;
    
    public QueryServiceImpl(ClientRepository clientRepository, 
                           OrderRepository orderRepository,
                           ServiceRepository serviceRepository) {
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.serviceRepository = serviceRepository;
    }
    
    @Override
    public List<Client> findClientsByEmail(String emailPattern) {
        return clientRepository.findByEmailContaining(emailPattern);
    }
    
    @Override
    public OrderStatisticsDto getOrderStatistics(LocalDate startDate, LocalDate endDate) {
        return orderRepository.getOrderStatistics(startDate, endDate);
    }
    
    @Override
    public List<org.application.autoService.model.Service> getTopExpensiveServices(int limit) {
        return serviceRepository.findTopByOrderByPriceDesc(limit);
    }
    
    @Override
    public ClientTotalDto getClientTotalOrders(Long clientId) {
        return orderRepository.getClientTotalOrders(clientId);
    }
    
    @Override
    public List<Order> findUnpaidOrdersAboveSum(Double minSum) {
        return orderRepository.findUnpaidOrdersAboveSum(minSum);
    }
}
