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
import org.gradle.api.Action
import org.gradle.api.file.DirectoryProperty
import java.io.File
import kotlin.reflect.KClass

abstract class MappingCompressionExtension {

    companion object {
        const val EXTENSION_NAME = "mappingCompression"
    }

    internal val strategies: MutableMap<String, CompressionStrategy> = linkedMapOf()
    abstract val mappingDirectory: DirectoryProperty
    abstract val outDirectory: DirectoryProperty

    fun with(strategy: KClass<out CompressionStrategy>, action: Action<StrategySpec>) {
        action.execute(StrategySpec(this, strategy))
    }

    inline fun <reified T : CompressionStrategy> with(action: Action<StrategySpec>) {
        with(T::class, action)
    }

}

class StrategySpec(
    private val extension: MappingCompressionExtension,
    private val strategy: KClass<out CompressionStrategy>
) {

    fun compress(vararg path: String) {
        val pathStr = path.joinToString(File.separator)
        extension.strategies[pathStr] =
            strategy.objectInstance ?: error("Invalid strategy class definition: $strategy")
    }

}
