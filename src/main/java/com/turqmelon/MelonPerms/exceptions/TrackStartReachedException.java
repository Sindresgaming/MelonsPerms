package com.turqmelon.MelonPerms.exceptions;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/

import com.turqmelon.MelonPerms.util.Track;

// Thrown when a user or group reaches the bottom of a track
public class TrackStartReachedException extends Exception {

    private Track track;

    public TrackStartReachedException(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }
}
