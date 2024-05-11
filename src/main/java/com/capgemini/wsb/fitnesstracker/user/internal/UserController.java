package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ok(userService.findAllUsers()
                          .stream()
                          .map(userMapper::toDto)
                          .toList());
    }

    @GetMapping("/getAllDetails")
    public ResponseEntity<List<UserDetailsDto>> getAllUsersDetails() {
        return ok(userService.findAllUsers()
                .stream()
                .map(userMapper::toUserDetailsDto)
                .toList());
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        final Optional<User> optionalUser = userService.getUser(userId);
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        return ok(userMapper.toDto(optionalUser.get()));
    }

    @GetMapping("/email/{emailPart}")
    public ResponseEntity<List<UserEmailDto>> getByEmail(@PathVariable String emailPart) {
        final List<User> userByEmail = userService.getUserByEmail(emailPart);
        if(userByEmail.isEmpty()) {
            throw new UserNotFoundException(emailPart);
        }
        return ok(userByEmail.stream().map(userMapper::toEmailDto).collect(toList()));
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userMapper.toEntity(userDto)), CREATED);
    }

    @PutMapping("/delete/{userId}")
    public ResponseEntity<Long> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(userId, NO_CONTENT);
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<List<UserDto>> getUsersOlderThanGivenAge(@PathVariable int age) {
        final List<User> olderUsers = userService.getUsersOlderThanProvided(age);
        if(olderUsers.isEmpty()) {
            throw new UserNotFoundException(valueOf(age));
        }
        return ok(olderUsers.stream().map(userMapper::toDto).collect(toList()));
    }

    @PutMapping("/changeUser")
    public ResponseEntity<User> updateUser(@RequestBody UserDto changedUser) {
        return new ResponseEntity<>(userService.updateUser(userMapper.toEntitySave(changedUser)), ACCEPTED);
    }

}