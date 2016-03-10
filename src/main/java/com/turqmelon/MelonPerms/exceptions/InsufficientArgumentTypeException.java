package com.turqmelon.MelonPerms.exceptions;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

// Thrown when the expected data does not match what was received.
public class InsufficientArgumentTypeException extends Exception {

    private String argument;
    private String received;
    private String expected;

    public InsufficientArgumentTypeException(String argument, String received, String expected) {
        this.argument = argument;
        this.received = received;
        this.expected = expected;
    }

    public String getArgument() {
        return argument;
    }

    public String getReceived() {
        return received;
    }

    public String getExpected() {
        return expected;
    }
}
