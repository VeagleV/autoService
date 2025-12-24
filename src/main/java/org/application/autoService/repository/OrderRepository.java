package org.application.autoService.repository;

import org.application.autoService.model.Order;
import org.application.autoService.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClient(Client client);
}
