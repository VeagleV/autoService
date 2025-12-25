package org.application.autoService.controller;

import org.application.autoService.dto.UserRequestDto;
import org.application.autoService.mapper.UserRequestMapper;
import org.application.autoService.model.UserRequest;
import org.application.autoService.service.UserRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
@Validated
@CrossOrigin
public class UserRequestController {

    private final UserRequestService userRequestService;
    private final UserRequestMapper userRequestMapper;

    public UserRequestController(UserRequestService userRequestService, UserRequestMapper userRequestMapper) {
        this.userRequestService = userRequestService;
        this.userRequestMapper = userRequestMapper;
    }

    @GetMapping
    public List<UserRequestDto> getAllUserRequests(@RequestParam(required = false) Long userId,
                                                    @RequestParam(required = false) String status) {
        List<UserRequest> requests;
        
        if (userId != null) {
            requests = userRequestService.getUserRequestsByUserId(userId);
        } else if (status != null) {
            try {
                UserRequest.RequestStatus requestStatus = UserRequest.RequestStatus.valueOf(status.toUpperCase());
                requests = userRequestService.getUserRequestsByStatus(requestStatus);
            } catch (IllegalArgumentException e) {
                return userRequestService.getAllUserRequests().stream()
                        .map(userRequestMapper::toDto)
                        .collect(Collectors.toList());
            }
        } else {
            requests = userRequestService.getAllUserRequests();
        }
        
        return requests.stream()
                .map(userRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequestDto> getUserRequestById(@PathVariable Long id) {
        return userRequestService.getUserRequestById(id)
                .map(userRequestMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserRequestDto> createUserRequest(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserRequest savedUserRequest = userRequestService.saveUserRequest(userRequestMapper.toEntity(userRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(userRequestMapper.toDto(savedUserRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRequestDto> updateUserRequest(@PathVariable Long id, @Valid @RequestBody UserRequestDto userRequestDto) {
        return userRequestService.getUserRequestById(id)
                .map(existing -> {
                    UserRequest updated = userRequestMapper.toEntity(userRequestDto);
                    updated.setRequestId(id);
                    updated.setCreatedAt(existing.getCreatedAt());
                    UserRequest saved = userRequestService.saveUserRequest(updated);
                    return ResponseEntity.ok(userRequestMapper.toDto(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRequest(@PathVariable Long id) {
        if (userRequestService.getUserRequestById(id).isPresent()) {
            userRequestService.deleteUserRequest(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
