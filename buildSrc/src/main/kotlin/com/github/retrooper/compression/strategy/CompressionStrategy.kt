/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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
package com.github.retrooper.compression.strategy

import java.nio.file.Path

interface CompressionStrategy {

    companion object {
        const val COMPRESSION_VERSION: Byte = 3

        fun resolveTargetPath(output: Path, relativePath: String): Path {
            return output.resolve(relativePath.replace(".json", ".nbt"))
        }
    }

    fun compress(from: Path, to: Path)

}
