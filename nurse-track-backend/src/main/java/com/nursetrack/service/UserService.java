package com.nursetrack.service;

import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.domain.model.User;
import com.nursetrack.exception.UserNotFoundException;
import com.nursetrack.exception.UserStatusConflictException;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.web.dto.request.user.CreateUserRequest;
import com.nursetrack.web.dto.request.user.UpdateUserRequest;
import com.nursetrack.web.dto.response.UserResponse;
import com.nursetrack.web.mappers.UserMapper;
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
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(String query,
                                          UserRole role,
                                          Boolean active,
                                          int page,
                                          int size,
                                          String sort)
    {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));

        // Si no hay filtros, usa findAll
        if (query == null && role == null && active == null)
        {
            return userRepository.findAll(pageable).map(userMapper::toDto);
        }

        // Si hay filtros, usa la búsqueda filtrada
        return userRepository.searchUsers(
                query != null ? query.toLowerCase() : null,
                role,
                active,
                pageable
        ).map(userMapper::toDto);
    }

    private Sort parseSort(String sort)
    {
        String[] parts = sort.split(",");

        return Sort.by(Sort.Direction.fromString(parts.length > 1 ? parts[1] : "asc"),parts[0]);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id)
    {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByRole(UserRole role, Pageable pageable)
    {
        return userRepository.findByRole(role, pageable).map(userMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getActiveUsers(Pageable pageable)
    {
        return userRepository.findByIsActive(true, pageable).map(userMapper::toDto);
    }

    public UserResponse createUser(CreateUserRequest request)
    {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);

        return userMapper.toDto(userRepository.save(user));
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userMapper.updateUserFromDto(request, user);
        return userMapper.toDto(userRepository.save(user));
    }

    public void toggleUserStatus(Long id, Boolean active)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (user.getIsActive() == active) throw new UserStatusConflictException(id, active);

        user.setIsActive(active);
        userRepository.save(user);
    }

    public void deleteUser(Long id)
    {
        if (!userRepository.existsById(id)) throw new UserNotFoundException(id);

        userRepository.deleteById(id);
    }
}