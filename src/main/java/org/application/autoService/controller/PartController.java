package org.application.autoService.controller;

import org.application.autoService.dto.PartDto;
import org.application.autoService.mapper.PartMapper;
import org.application.autoService.model.Part;
import org.application.autoService.service.PartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parts")
@Validated
@CrossOrigin
public class PartController {

    private final PartService partService;
    private final PartMapper partMapper;

    public PartController(PartService partService, PartMapper partMapper) {
        this.partService = partService;
        this.partMapper = partMapper;
    }

    @GetMapping
    public List<PartDto> getAllParts() {
        return partService.getAllParts().stream()
                .map(partMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartDto> getPartById(@PathVariable Long id) {
        return partService.getPartById(id)
                .map(partMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PartDto> createPart(@Valid @RequestBody PartDto dto) {
        Part saved = partService.savePart(partMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(partMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartDto> updatePart(@PathVariable Long id, @Valid @RequestBody PartDto dto) {
        return partService.getPartById(id)
                .map(existing -> {
                    Part updated = partMapper.toEntity(dto);
                    updated.setPartId(id);
                    Part saved = partService.savePart(updated);
                    return ResponseEntity.ok(partMapper.toDto(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePart(@PathVariable Long id) {
        if (partService.getPartById(id).isPresent()) {
            partService.deletePart(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
