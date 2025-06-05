package com.nurse_track_back.nurse_track_back.services;

import com.nurse_track_back.nurse_track_back.domain.models.User;
import com.nurse_track_back.nurse_track_back.repositories.SupervisorDepartmentRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.user.CreateUserRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.user.UpdateUserRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.UserResponse;
import com.nurse_track_back.nurse_track_back.exceptions.InvalidStatusException;
import com.nurse_track_back.nurse_track_back.exceptions.ResourceNotFoundException;
import com.nurse_track_back.nurse_track_back.web.mappers.UserMapper;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final SupervisorDepartmentRepository supervisorDepartmentRepository;

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.map(userMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> ResourceNotFoundException.create("User", id));
    }

    public UserResponse createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);

        return userMapper.toDTO(userRepository.save(user));
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("User", id));

        /*// Actualizar contraseña si se proporciona
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }*/

        userMapper.updateModel(request, user);
        return userMapper.toDTO(userRepository.save(user));
    }

    public void toggleUserStatus(Long id, Boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("User", id));

        if (user.getIsActive() == active) {
            throw InvalidStatusException.create("active", user.getId());
        }

        user.setIsActive(active);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("User", id));

        if (user.getRole() == com.nurse_track_back.nurse_track_back.domain.enums.Role.ROLE_SUPERVISOR) {
            log.info("Deleting supervisor department assignments for user ID: {}", id);
            supervisorDepartmentRepository.deleteBySupervisorId(id);
            // Remove: entityManager.flush(); // No longer needed here
            log.info("Supervisor department assignments deleted for user ID: {}", id); // Update log message
        }

        log.info("Attempting to delete user ID: {}", id);
        userRepository.deleteById(id);
        log.info("User ID: {} deleted successfully.", id);
    }

}
