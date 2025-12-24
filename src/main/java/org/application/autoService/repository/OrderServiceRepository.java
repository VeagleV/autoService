package org.application.autoService.repository;

import org.application.autoService.model.OrderService;
import org.application.autoService.model.Order;
import org.application.autoService.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderServiceRepository extends JpaRepository<OrderService, Long> {
    List<OrderService> findByOrder(Order order);
    List<OrderService> findByService(Service service);
}
