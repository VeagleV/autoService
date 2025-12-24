package org.application.autoService.service;

import org.application.autoService.model.Part;
import java.util.List;
import java.util.Optional;

public interface PartService {
    Part savePart(Part part);
    Optional<Part> getPartById(Long id);
    List<Part> getAllParts();
    void deletePart(Long id);
}
