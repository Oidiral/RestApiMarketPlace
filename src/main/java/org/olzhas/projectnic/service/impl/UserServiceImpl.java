package org.olzhas.projectnic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olzhas.projectnic.dto.UsersDto;
import org.olzhas.projectnic.entity.User;
import org.olzhas.projectnic.exception.NotFoundException;
import org.olzhas.projectnic.mapper.UsersMapper;
import org.olzhas.projectnic.repository.UserRepository;
import org.olzhas.projectnic.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final UsersMapper usersMapper;
    final JWTService jwtService;
    final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UsersDto updateUser(long id, UsersDto usersDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found"));
        if (!passwordEncoder.matches(usersDto.getPassword(), user.getPassword())) {
            usersDto.setPassword(passwordEncoder.encode(usersDto.getPassword()));
        }
        usersMapper.partialUpdate(usersDto, user);
        userRepository.save(user);
        log.info("User updated id {}", user.getId());
        return usersMapper.toDto(user);
    }

    @Transactional
    @Override
    @PreAuthorize("authentication.principal.id == #id or hasRole('ROLE_ADMIN')")
    public void deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            log.warn("User with id {} not found", id);
            throw new NotFoundException("User not found");
        }
        log.info("User deleted id {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public UsersDto findByUserId(long id) {
        return usersMapper.toDto(userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found")));
    }
}
