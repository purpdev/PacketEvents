/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.compression.strategy.dir

import com.github.retrooper.compression.strategy.CompressionStrategy
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.walk

abstract class DirCompressionStrategy(
    private val strategy: CompressionStrategy
) : CompressionStrategy {

    @OptIn(ExperimentalPathApi::class)
    override fun compress(from: Path, to: Path) {
        from.walk().forEach {
            val relativePath = from.relativize(it).toString()
            val target = CompressionStrategy.resolveTargetPath(to, relativePath)
            this.strategy.compress(it, target)
        }
    }
}
