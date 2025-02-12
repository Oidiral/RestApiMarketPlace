package org.olzhas.projectnic.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.olzhas.projectnic.dto.UsersDto;
import org.olzhas.projectnic.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @PatchMapping("/update/{id}")
    public ResponseEntity<UsersDto> updateUser(@PathVariable("id") long id, @RequestBody @Valid UsersDto usersDto) {
        UsersDto updatedUser = userServiceImpl.updateUser(id, usersDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<UsersDto> getUser(@PathVariable("id") long id) {
        return new ResponseEntity<>(userServiceImpl.findByUserId(id), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        userServiceImpl.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

