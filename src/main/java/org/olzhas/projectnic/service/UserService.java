package org.olzhas.projectnic.service;

import org.olzhas.projectnic.dto.UsersDto;

public interface UserService {
    UsersDto updateUser(long id, UsersDto user);

    void deleteUser(long id);

    UsersDto findByUserId(long id);
}
