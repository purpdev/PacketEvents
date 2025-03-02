/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;

public class VersionMapper {
    private final ClientVersion[] versions;
    private final ClientVersion[] reversedVersions;

    public VersionMapper(ClientVersion... versions) {
        this.versions = versions;
        reversedVersions = new ClientVersion[versions.length];
        int index = 0;
        for (int i = versions.length - 1; i >= 0; i--) {
            reversedVersions[index] = versions[i];
            index++;
        }
    }

    public ClientVersion[] getVersions() {
        return versions;
    }

    public ClientVersion[] getReversedVersions() {
        return reversedVersions;
    }

    public int getIndex(ClientVersion version) {
        int index = reversedVersions.length - 1;
        for (ClientVersion v : reversedVersions) {
            if (version.isNewerThanOrEquals(v)) {
                return index;
            }
            index--;
        }

        //Give them the oldest version
        return 0;
    }
}
