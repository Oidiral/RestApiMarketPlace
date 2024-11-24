package org.olzhas.projectnic.service;

import org.olzhas.projectnic.dto.UsersDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UsersDto updateUser(long id, UsersDto user);

    void deleteUser(long id);

    UsersDto findByUserId(long id);
}
