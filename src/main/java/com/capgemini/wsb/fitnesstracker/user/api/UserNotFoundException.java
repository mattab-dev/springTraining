package com.capgemini.wsb.fitnesstracker.user.api;

import com.capgemini.wsb.fitnesstracker.exception.api.NotFoundException;

/**
 * Exception indicating that the {@link User} was not found.
 */
@SuppressWarnings("squid:S110")
public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String messageArg) {
        super("User was not found by property value: %s".formatted(messageArg));
    }

    public UserNotFoundException(Long id) {
        super("User with ID=%s was not found".formatted(id));
    }

}
