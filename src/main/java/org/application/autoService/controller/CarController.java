package org.application.autoService.controller;

import org.application.autoService.dto.CarDto;
import org.application.autoService.mapper.CarMapper;
import org.application.autoService.model.Car;
import org.application.autoService.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
@Validated
@CrossOrigin
public class CarController {

    private final CarService carService;
    private final CarMapper carMapper;

    public CarController(CarService carService, CarMapper carMapper) {
        this.carService = carService;
        this.carMapper = carMapper;
    }

    @GetMapping
    public List<CarDto> getAllCars() {
        return carService.getAllCars().stream()
                .map(carMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long id) {
        return carService.getCarById(id)
                .map(carMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CarDto> createCar(@Valid @RequestBody CarDto carDto) {
        Car savedCar = carService.saveCar(carMapper.toEntity(carDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(carMapper.toDto(savedCar));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable Long id, @Valid @RequestBody CarDto carDto) {
        return carService.getCarById(id)
                .map(existing -> {
                    Car updatedCar = carMapper.toEntity(carDto);
                    updatedCar.setCarId(id);
                    Car saved = carService.saveCar(updatedCar);
                    return ResponseEntity.ok(carMapper.toDto(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        if (carService.getCarById(id).isPresent()) {
            carService.deleteCar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
