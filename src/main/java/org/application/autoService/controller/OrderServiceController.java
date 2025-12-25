package org.application.autoService.controller;

import org.application.autoService.dto.OrderServiceDto;
import org.application.autoService.mapper.OrderServiceMapper;
import org.application.autoService.model.OrderService;
import org.application.autoService.service.OrderServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orderservices")
@Validated
@CrossOrigin
public class OrderServiceController {

    private final OrderServiceService orderServiceService;
    private final OrderServiceMapper orderServiceMapper;

    public OrderServiceController(OrderServiceService orderServiceService, OrderServiceMapper orderServiceMapper) {
        this.orderServiceService = orderServiceService;
        this.orderServiceMapper = orderServiceMapper;
    }

    @GetMapping
    public List<OrderServiceDto> getAll() {
        return orderServiceService.getAll().stream()
                .map(orderServiceMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderServiceDto> getById(@PathVariable Long id) {
        return orderServiceService.getById(id)
                .map(orderServiceMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderServiceDto> create(@Valid @RequestBody OrderServiceDto dto) {
        OrderService saved = orderServiceService.save(orderServiceMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(orderServiceMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderServiceDto> update(@PathVariable Long id, @Valid @RequestBody OrderServiceDto dto) {
        return orderServiceService.getById(id)
                .map(existing -> {
                    OrderService updated = orderServiceMapper.toEntity(dto);
                    updated.setOrderServiceId(id);
                    OrderService saved = orderServiceService.save(updated);
                    return ResponseEntity.ok(orderServiceMapper.toDto(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (orderServiceService.getById(id).isPresent()) {
            orderServiceService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
