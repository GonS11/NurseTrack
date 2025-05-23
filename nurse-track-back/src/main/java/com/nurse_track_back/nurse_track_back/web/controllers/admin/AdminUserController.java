package com.nurse_track_back.nurse_track_back.web.controllers.admin;

import com.nurse_track_back.nurse_track_back.web.dto.request.user.CreateUserRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.user.UpdateUserRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.UserResponse;
import com.nurse_track_back.nurse_track_back.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(userService.getAllUsers(page, size, sortBy));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("userId") Long userId,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @PutMapping("/{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable("userId") Long userId) {
        userService.toggleUserStatus(userId, true);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/desactivate")
    public ResponseEntity<Void> desactivateUser(@PathVariable("userId") Long userId) {
        userService.toggleUserStatus(userId, false);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
