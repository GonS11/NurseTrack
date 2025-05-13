package com.nursetrack.web.controller.admin;

import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.service.UserService;
import com.nursetrack.web.dto.request.user.CreateUserRequest;
import com.nursetrack.web.dto.request.user.UpdateUserRequest;
import com.nursetrack.web.dto.response.UserResponse;
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
//@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController
{
    private final UserService userService;

    @GetMapping //Todos y con busqueda a la vez
    public ResponseEntity<Page<UserResponse>> getUsers(@RequestParam(required = false) String query,
                                                       @RequestParam(required = false) UserRole role,
                                                       @RequestParam(required = false) Boolean active,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "lastName,asc") String sort)
    {
        return ResponseEntity.ok(userService.searchUsers(query, role, active, page, size, sort));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId)
    {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
                                                   @Valid @RequestBody UpdateUserRequest request)
    {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @PutMapping("/{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long userId)
    {
        userService.toggleUserStatus(userId, true);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId)
    {
        userService.toggleUserStatus(userId, false);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId)
    {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
