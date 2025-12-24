package org.application.autoService.repository;

import org.application.autoService.model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    List<UserRequest> findByUserId(Long userId);
    List<UserRequest> findByStatus(UserRequest.RequestStatus status);
}
