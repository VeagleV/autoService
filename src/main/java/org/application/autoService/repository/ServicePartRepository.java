package org.application.autoService.repository;

import org.application.autoService.model.ServicePart;
import org.application.autoService.model.Service;
import org.application.autoService.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicePartRepository extends JpaRepository<ServicePart, Long> {
    List<ServicePart> findByService(Service service);
    List<ServicePart> findByPart(Part part);
}
