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

import com.github.steveice10.opennbt.tag.builtin.Tag
import com.github.steveice10.opennbt.tag.io.NBTIO
import com.github.steveice10.opennbt.tag.io.TagWriter
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import java.io.IOException
import java.nio.file.Path
import java.util.*
import kotlin.io.path.bufferedReader

object CompressionUtil {

    val GSON: Gson = Gson()
    val TAG_WRITER: TagWriter = NBTIO.writer().named()

    fun loadJson(path: Path): JsonElement {
        try {
            path.bufferedReader().use {
                return GSON.fromJson(it, JsonElement::class.java)
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to load json file: $path", e)
        }
    }

    fun writeNbt(path: Path, tag: Tag) {
        try {
            TAG_WRITER.write(path, tag, true)
        } catch (e: IOException) {
            throw RuntimeException("Failed to write nbt file: $path", e)
        }
    }

}

val JsonArray.asStringList: List<String>
    get() = map { it.asString }

val JsonObject.asPrimitiveMap: Map<String, JsonPrimitive>
    get() = entrySet().asSequence()
        .filter { it.value.isJsonPrimitive }
        .associateTo(TreeMap()) { it.key to it.value.asJsonPrimitive }
