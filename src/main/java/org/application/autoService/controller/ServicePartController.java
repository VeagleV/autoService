package org.application.autoService.controller;

import org.application.autoService.dto.ServicePartDto;
import org.application.autoService.mapper.ServicePartMapper;
import org.application.autoService.model.ServicePart;
import org.application.autoService.service.ServicePartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/serviceparts")
@Validated
@CrossOrigin
public class ServicePartController {

    private final ServicePartService servicePartService;
    private final ServicePartMapper servicePartMapper;

    public ServicePartController(ServicePartService servicePartService, ServicePartMapper servicePartMapper) {
        this.servicePartService = servicePartService;
        this.servicePartMapper = servicePartMapper;
    }

    @GetMapping
    public List<ServicePartDto> getAll() {
        return servicePartService.getAll().stream()
                .map(servicePartMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicePartDto> getById(@PathVariable Long id) {
        return servicePartService.getById(id)
                .map(servicePartMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServicePartDto> create(@Valid @RequestBody ServicePartDto dto) {
        ServicePart saved = servicePartService.save(servicePartMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(servicePartMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicePartDto> update(@PathVariable Long id, @Valid @RequestBody ServicePartDto dto) {
        return servicePartService.getById(id)
                .map(existing -> {
                    ServicePart updated = servicePartMapper.toEntity(dto);
                    updated.setServicePartId(id);
                    ServicePart saved = servicePartService.save(updated);
                    return ResponseEntity.ok(servicePartMapper.toDto(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (servicePartService.getById(id).isPresent()) {
            servicePartService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
