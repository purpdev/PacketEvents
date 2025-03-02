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
package com.github.retrooper.compression

import com.github.retrooper.compression.strategy.CompressionStrategy
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path

abstract class MappingCompressionTask : DefaultTask() {

    companion object {
        const val TASK_NAME = "compressMappings"
    }

    @get:Internal
    internal var mappingsDir: Path? = null

    @get:Internal
    internal var outDir: Path? = null

    @get:Internal
    internal var strategies: Map<String, CompressionStrategy> = emptyMap()

    @TaskAction
    fun compress() {
        mappingsDir ?: error("mappingsDir is not set")
        outDir ?: error("outDir is not set")

        for ((relativePath, strategy) in strategies) {
            val target = CompressionStrategy.resolveTargetPath(outDir!!, relativePath)
            strategy.compress(mappingsDir!!.resolve(relativePath), target)
        }
    }
}
