package org.application.autoService.controller;

import org.application.autoService.dto.ServiceDto;
import org.application.autoService.mapper.ServiceMapper;
import org.application.autoService.model.Service;
import org.application.autoService.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
@Validated
public class ServiceController {

    private final ServiceService serviceService;
    private final ServiceMapper serviceMapper;

    public ServiceController(ServiceService serviceService, ServiceMapper serviceMapper) {
        this.serviceService = serviceService;
        this.serviceMapper = serviceMapper;
    }

    @GetMapping
    public List<ServiceDto> getAllServices() {
        return serviceService.getAllServices().stream()
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id)
                .map(serviceMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceDto> createService(@Valid @RequestBody ServiceDto dto) {
        Service saved = serviceService.saveService(serviceMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDto> updateService(@PathVariable Long id, @Valid @RequestBody ServiceDto dto) {
        return serviceService.getServiceById(id)
                .map(existing -> {
                    Service updated = serviceMapper.toEntity(dto);
                    updated.setServiceId(id);
                    Service saved = serviceService.saveService(updated);
                    return ResponseEntity.ok(serviceMapper.toDto(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        if (serviceService.getServiceById(id).isPresent()) {
            serviceService.deleteService(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
