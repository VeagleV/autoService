package org.application.autoService.service.impl;

import org.application.autoService.model.UserRequest;
import org.application.autoService.repository.UserRequestRepository;
import org.application.autoService.service.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRequestServiceImpl implements UserRequestService {
    private final UserRequestRepository userRequestRepository;

    @Autowired
    public UserRequestServiceImpl(UserRequestRepository userRequestRepository) {
        this.userRequestRepository = userRequestRepository;
    }

    @Override
    public UserRequest saveUserRequest(UserRequest userRequest) {
        return userRequestRepository.save(userRequest);
    }

    @Override
    public Optional<UserRequest> getUserRequestById(Long id) {
        return userRequestRepository.findById(id);
    }

    @Override
    public List<UserRequest> getAllUserRequests() {
        return userRequestRepository.findAll();
    }

    @Override
    public List<UserRequest> getUserRequestsByUserId(Long userId) {
        return userRequestRepository.findByUserId(userId);
    }

    @Override
    public List<UserRequest> getUserRequestsByStatus(UserRequest.RequestStatus status) {
        return userRequestRepository.findByStatus(status);
    }

    @Override
    public void deleteUserRequest(Long id) {
        userRequestRepository.deleteById(id);
    }
}
