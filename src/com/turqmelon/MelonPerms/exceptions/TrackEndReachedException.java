package com.turqmelon.MelonPerms.exceptions;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

import com.turqmelon.MelonPerms.util.Track;

// Thrown when a user or group reaches the top of a track
public class TrackEndReachedException extends Exception {

    private Track track;

    public TrackEndReachedException(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }
}
