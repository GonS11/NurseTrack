package com.nurse_track_back.nurse_track_back.service;

import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import com.nurse_track_back.nurse_track_back.domain.model.User;
import com.nurse_track_back.nurse_track_back.dto.request.user.CreateUserRequest;
import com.nurse_track_back.nurse_track_back.dto.request.user.UpdateUserRequest;
import com.nurse_track_back.nurse_track_back.dto.response.UserResponse;
import com.nurse_track_back.nurse_track_back.exception.UserNotFoundException;
import com.nurse_track_back.nurse_track_back.exception.UserStatusConflictException;
import com.nurse_track_back.nurse_track_back.mapper.UserMapper;
import com.nurse_track_back.nurse_track_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<UserResponse> getAllUsers(int page, int size, String sortBy)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.map(userMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id)
    {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserResponse createUser(CreateUserRequest request)
    {
        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);

        return userMapper.toDTO(userRepository.save(user));
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Actualizar contraseña si se proporciona
        if (request.getPassword() != null && !request.getPassword().isBlank())
        {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userMapper.updateModel(request, user);
        return userMapper.toDTO(userRepository.save(user));
    }

    public void toggleUserStatus(Long id, Boolean active)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (user.getIsActive() == active)
        {
            throw new UserStatusConflictException(id, active);
        }

        user.setIsActive(active);
        userRepository.save(user);
    }

    public void deleteUser(Long id)
    {
        if (!userRepository.existsById(id)) throw new UserNotFoundException(id);

        userRepository.deleteById(id);
    }

}
