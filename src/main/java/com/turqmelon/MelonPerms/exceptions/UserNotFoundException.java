package com.turqmelon.MelonPerms.exceptions;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

// Thrown when the provided name or UUID doesn't match anybody in the DataStore
public class UserNotFoundException extends Exception {

    private String user;

    public UserNotFoundException(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
