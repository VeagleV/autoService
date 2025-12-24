package org.application.autoService.service;

import org.application.autoService.model.Car;
import java.util.List;
import java.util.Optional;

public interface CarService {
    Car saveCar(Car car);
    Optional<Car> getCarById(Long id);
    List<Car> getAllCars();
    void deleteCar(Long id);
}
