package com.turqmelon.MelonPerms.exceptions;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

// Thrown when the supplied name doesn't match any tracks
public class TrackNotFoundException extends Exception {

    private String track;

    public TrackNotFoundException(String track) {
        this.track = track;
    }

    public String getTrack() {
        return track;
    }
}
