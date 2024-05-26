package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserDto;
import com.capgemini.wsb.fitnesstracker.user.api.UserEmailDto;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/simple")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ok(userService.findAllUsers()
                          .stream()
                          .map(userMapper::toDto)
                          .toList());
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsersDetails() {
        return ok(userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        final Optional<User> optionalUser = userService.getUser(userId);
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        return ok(userMapper.toDto(optionalUser.get()));
    }

    @GetMapping("/email")
    public List<UserEmailDto> getByEmail(@RequestParam String email) {
        final List<User> userByEmail = userService.getUserByEmail(email);
        if(userByEmail.isEmpty()) {
            throw new UserNotFoundException(email);
        }
        return userByEmail.stream().map(userMapper::toEmailDto).collect(toList());
    }

    @PostMapping()
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userMapper.toEntity(userDto)), CREATED);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{userId}")
    public ResponseEntity<Long> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(userId, NO_CONTENT);
    }

    @GetMapping("/older/{time}")
    public ResponseEntity<List<UserDto>> getUsersOlderThanGivenAge(@PathVariable LocalDate time) {
        final List<User> olderUsers = userService.getUsersOlderThanProvided(time);
        if(olderUsers.isEmpty()) {
            throw new UserNotFoundException(valueOf(time));
        }
        return ok(olderUsers.stream().map(userMapper::toDto).collect(toList()));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserDto changedUser) {
        return new ResponseEntity<>(userService.updateUser(userId, userMapper.toEntitySave(changedUser)), ACCEPTED);
    }

}