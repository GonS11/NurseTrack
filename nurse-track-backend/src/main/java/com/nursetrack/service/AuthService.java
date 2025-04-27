package com.nursetrack.service;

import com.nursetrack.domain.model.User;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.security.jwt.JwtUtil;
import com.nursetrack.web.dto.request.auth.LoginRequest;
import com.nursetrack.web.dto.response.CurrentUserResponse;
import com.nursetrack.web.dto.response.LoginResponse;
import com.nursetrack.web.mappers.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService
{
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthMapper authMapper;

    public LoginResponse login(LoginRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getIsActive()) throw new DisabledException("User account is inactive");

        String token = jwtUtil.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                )
        );

        user.setJwtToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusDays(1));
        userRepository.save(user);

        return authMapper.toLoginResponse(user, token);
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse getCurrentUser(String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return authMapper.toCurrentUserResponse(user);
    }

    public void logout(String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setJwtToken(null);
        user.setTokenExpiry(null);

        userRepository.save(user);
    }
}