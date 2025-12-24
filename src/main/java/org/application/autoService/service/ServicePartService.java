package org.application.autoService.service;

import org.application.autoService.model.ServicePart;
import java.util.List;
import java.util.Optional;

public interface ServicePartService {
    ServicePart save(ServicePart servicePart);
    Optional<ServicePart> getById(Long id);
    List<ServicePart> getAll();
    void delete(Long id);
}
