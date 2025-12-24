package org.application.autoService.service.impl;

import org.application.autoService.model.OrderService;
import org.application.autoService.repository.OrderServiceRepository;
import org.application.autoService.service.OrderServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceServiceImpl implements OrderServiceService {
    private final OrderServiceRepository orderServiceRepository;

    @Autowired
    public OrderServiceServiceImpl(OrderServiceRepository orderServiceRepository) {
        this.orderServiceRepository = orderServiceRepository;
    }

    @Override
    public OrderService save(OrderService orderService) {
        return orderServiceRepository.save(orderService);
    }

    @Override
    public Optional<OrderService> getById(Long id) {
        return orderServiceRepository.findById(id);
    }

    @Override
    public List<OrderService> getAll() {
        return orderServiceRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        orderServiceRepository.deleteById(id);
    }
}
