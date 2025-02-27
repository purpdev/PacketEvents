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

package com.github.retrooper.compression.strategy.json

import com.github.retrooper.compression.strategy.CompressionStrategy
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import java.nio.file.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.reader

object JsonRegistryCompressionStrategy : CompressionStrategy {

    override fun compress(from: Path, to: Path) {
        // determine strategy based on first json token
        val strategy: CompressionStrategy?
        JsonReader(from.bufferedReader()).use {
            it.beginObject()
            it.nextName()
            val firstToken = it.peek()
            strategy = when (firstToken) {
                JsonToken.BEGIN_ARRAY -> JsonArrayCompressionStrategy
                JsonToken.BEGIN_OBJECT -> JsonObjectCompressionStrategy
                else -> null
            }
        }

        // error if no strategy has been found; otherwise, compress it!
        if (strategy == null) {
            throw IllegalStateException("Can't determine compression strategy for $from")
        }
        strategy.compress(from, to)
    }
}
