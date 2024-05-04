package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserDetailsDto;
import com.capgemini.wsb.fitnesstracker.user.api.UserDto;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
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

    @GetMapping("/getById")
    public ResponseEntity<UserDto> getUser(@RequestBody Long userId) {
        final Optional<User> optionalUser = userService.getUser(userId);
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        return ok(userMapper.toDto(optionalUser.get()));
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<List<User>> getByEmail(@RequestBody String email) {

        return ok(null);
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userMapper.toEntity(userDto)), CREATED);
    }

    @PutMapping("/delete")
    public ResponseEntity<Long> deleteUser(@RequestBody Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(userId, NO_CONTENT);
    }

}