package org.application.autoService.repository;

import org.application.autoService.model.Car;
import org.application.autoService.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByClient(Client client);
}
