package com.turqmelon.MelonPerms.util;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 ******************************************************************************/
import com.turqmelon.MelonPerms.exceptions.TrackEndReachedException;
import com.turqmelon.MelonPerms.exceptions.TrackGroupNotDefinedException;
import com.turqmelon.MelonPerms.exceptions.TrackNoGroupsDefinedException;
import com.turqmelon.MelonPerms.exceptions.TrackStartReachedException;
import com.turqmelon.MelonPerms.groups.Group;

import java.util.ArrayList;
import java.util.List;

public class Track {

    private String name;
    private List<Group> groups = new ArrayList<>();
    private boolean defaultTrack = false;

    public Track(String name) {
        this.name = name;
    }

    public Group getPrevious(Group group) throws TrackNoGroupsDefinedException, TrackGroupNotDefinedException, TrackStartReachedException {
        if (getGroups().size() == 0)
            throw new TrackNoGroupsDefinedException(this);

        for (int i = 0; i < getGroups().size(); i++) {
            Group g = getGroups().get(i);
            if (g.getName().equals(group.getName())) {
                if ((i - 1) < 0) {
                    throw new TrackStartReachedException(this);
                }
                return getGroups().get(i - 1);
            }
        }

        throw new TrackGroupNotDefinedException(this, group);

    }

    public Group getNext(Group group) throws TrackNoGroupsDefinedException, TrackGroupNotDefinedException, TrackEndReachedException {
        if (getGroups().size() == 0)
            throw new TrackNoGroupsDefinedException(this);

        for (int i = 0; i < getGroups().size(); i++) {
            Group g = getGroups().get(i);
            if (g.getName().equals(group.getName())) {
                if ((i + 1) >= getGroups().size()) {
                    throw new TrackEndReachedException(this);
                }
                return getGroups().get(i + 1);
            }
        }

        throw new TrackGroupNotDefinedException(this, group);

    }

    public boolean isDefaultTrack() {
        return defaultTrack;
    }

    public void setDefaultTrack(boolean defaultTrack) {
        this.defaultTrack = defaultTrack;
    }

    public String getName() {
        return name;
    }

    public List<Group> getGroups() {
        return groups;
    }
}
