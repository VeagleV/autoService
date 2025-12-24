package org.application.autoService.service;

import org.application.autoService.model.UserRequest;
import java.util.List;
import java.util.Optional;

public interface UserRequestService {
    UserRequest saveUserRequest(UserRequest userRequest);
    Optional<UserRequest> getUserRequestById(Long id);
    List<UserRequest> getAllUserRequests();
    List<UserRequest> getUserRequestsByUserId(Long userId);
    List<UserRequest> getUserRequestsByStatus(UserRequest.RequestStatus status);
    void deleteUserRequest(Long id);
}
