package com.taihwa.tutorial.service;

import com.taihwa.tutorial.dto.UserDto;
import com.taihwa.tutorial.entity.Authority;
import com.taihwa.tutorial.entity.User;
import com.taihwa.tutorial.repository.UserRepository;
import com.taihwa.tutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder().authorityName("ROLE_USER").build();

        User user = User.builder().
                username(userDto.getUsername()).
                password(passwordEncoder.encode(userDto.getPassword())).
                nickname(userDto.getNickname()).
                authorities(Collections.singleton(authority)).
                activated(true).
                build();
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }
}
