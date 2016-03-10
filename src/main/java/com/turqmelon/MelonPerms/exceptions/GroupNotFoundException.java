package com.turqmelon.MelonPerms.exceptions;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

// Thrown when the supplied name doesn't match any groups
public class GroupNotFoundException extends Exception {

    private String group;

    public GroupNotFoundException(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }
}
