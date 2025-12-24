package org.application.autoService.service;

import org.application.autoService.model.Service;
import java.util.List;
import java.util.Optional;

public interface ServiceService {
    Service saveService(Service service);
    Optional<Service> getServiceById(Long id);
    List<Service> getAllServices();
    void deleteService(Long id);
}
