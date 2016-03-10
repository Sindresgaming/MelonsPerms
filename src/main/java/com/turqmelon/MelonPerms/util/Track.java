package com.turqmelon.MelonPerms.util;

/*******************************************************************************
 * Copyright (c) 2016.  Written by Devon "Turqmelon": http://turqmelon.com
 * For more information, see LICENSE.TXT.
 * <p>
 * Written by Devon "Turqmelon" - http://turqmelon.com
 * <p>
 * This code is licensed under Creative Commons Attributation-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * <p>
 * You are free to:
 * SHARE - copy and redistribute the material in any medium or format
 * ADAPT - remix, transform, and build upon the material
 * <p>
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 * <p>
 * Under the following terms:
 * ATTRIBUTION - You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 * <p>
 * NONCOMMERCIAL - You may not use the material for commercial purposes.
 * <p>
 * SHAREALIKE - If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 * <p>
 * Full License: http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 ******************************************************************************/

/*******************************************************************************
 * Written by Devon "Turqmelon" - http://turqmelon.com
 *
 * This code is licensed under Creative Commons Attributation-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 *
 * You are free to:
 * SHARE - copy and redistribute the material in any medium or format
 * ADAPT - remix, transform, and build upon the material
 *
 * The licensor cannot revoke these freedoms as long as you follow the license terms.
 *
 * Under the following terms:
 * ATTRIBUTION - You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 *
 * NONCOMMERCIAL - You may not use the material for commercial purposes.
 *
 * SHAREALIKE - If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
 *
 * Full License: http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
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
