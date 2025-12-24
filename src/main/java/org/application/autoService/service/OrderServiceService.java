package org.application.autoService.service;

import org.application.autoService.model.OrderService;
import java.util.List;
import java.util.Optional;

public interface OrderServiceService {
    OrderService save(OrderService orderService);
    Optional<OrderService> getById(Long id);
    List<OrderService> getAll();
    void delete(Long id);
}
