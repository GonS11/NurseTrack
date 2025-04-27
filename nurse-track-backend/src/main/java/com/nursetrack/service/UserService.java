package com.nursetrack.service;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<UserResponse> getAllUsers()
    {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id)
    {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
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

    public void toggleUserStatus(Long id, boolean active)
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