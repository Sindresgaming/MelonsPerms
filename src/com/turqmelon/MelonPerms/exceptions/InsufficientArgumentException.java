package com.turqmelon.MelonPerms.exceptions;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

// Thrown when not enough arguments are passed to a command
public class InsufficientArgumentException extends Exception {

    private int required;

    public InsufficientArgumentException(int required) {
        this.required = required;
    }

    public int getRequired() {
        return required;
    }
}
