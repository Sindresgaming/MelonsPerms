package com.turqmelon.MelonPerms.exceptions;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/


import com.turqmelon.MelonPerms.util.Track;

// Thrown when a user is attempted to be moved in a track without any groups
public class TrackNoGroupsDefinedException extends Exception {

    private Track track;

    public TrackNoGroupsDefinedException(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }
}
