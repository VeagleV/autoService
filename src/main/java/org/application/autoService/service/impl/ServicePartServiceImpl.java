package org.application.autoService.service.impl;

import org.application.autoService.model.ServicePart;
import org.application.autoService.repository.ServicePartRepository;
import org.application.autoService.service.ServicePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicePartServiceImpl implements ServicePartService {
    private final ServicePartRepository servicePartRepository;

    @Autowired
    public ServicePartServiceImpl(ServicePartRepository servicePartRepository) {
        this.servicePartRepository = servicePartRepository;
    }

    @Override
    public ServicePart save(ServicePart servicePart) {
        return servicePartRepository.save(servicePart);
    }

    @Override
    public Optional<ServicePart> getById(Long id) {
        return servicePartRepository.findById(id);
    }

    @Override
    public List<ServicePart> getAll() {
        return servicePartRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        servicePartRepository.deleteById(id);
    }
}
