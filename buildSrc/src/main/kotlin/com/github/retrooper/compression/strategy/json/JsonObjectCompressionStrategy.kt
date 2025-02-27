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

import com.github.difflib.DiffUtils
import com.github.difflib.algorithm.myers.MeyersDiff
import com.github.difflib.patch.ChangeDelta
import com.github.difflib.patch.DeleteDelta
import com.github.difflib.patch.InsertDelta
import com.github.retrooper.compression.asPrimitiveMap
import com.github.steveice10.opennbt.tag.builtin.*
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

object JsonObjectCompressionStrategy : JsonCompressionStrategy() {

    override fun serialize(json: JsonElement): Tag {
        val entries = separateVersions(json.asJsonObject)

        val tag = CompoundTag()
        tag.put("length", ByteTag(entries.size.toByte()))

        // we will remove the first entry, which is the oldest version
        val oldestVersion = entries.pollFirstEntry()

        val nbtEntries = CompoundTag()
        var ent = oldestVersion.value.asJsonObject.asPrimitiveMap

        val oldestVersionTag = CompoundTag()
        for ((key, value) in ent) {
            oldestVersionTag.put(key, value.asNbtTag)
        }
        nbtEntries.put(oldestVersion.key.toString(), oldestVersionTag)

        for ((key, value) in entries) {
            val map = value.asJsonObject.asPrimitiveMap
            val algo = MeyersDiff.factory().create<Map.Entry<String, JsonPrimitive>> { a, b -> a == b }
            val diff = DiffUtils.diff(ArrayList(ent.entries), ArrayList(map.entries), algo, null, false)

            val versionTag = CompoundTag()
            val additions = CompoundTag()
            val removals = CompoundTag()
            for (delta in diff.deltas) {
                when (delta) {
                    is InsertDelta<*> -> {
                        for ((k, v) in delta.getTarget().lines) {
                            additions.put(k, v.asNbtTag)
                        }
                    }

                    is DeleteDelta<*> -> {
                        for ((k, v) in delta.getSource().lines) {
                            removals.put(k, v.asNbtTag)
                        }
                    }

                    is ChangeDelta<*> -> {
                        for ((k, v) in delta.getSource().lines) {
                            removals.put(k, v.asNbtTag)
                        }
                        for ((k, v) in delta.getTarget().lines) {
                            additions.put(k, v.asNbtTag)
                        }
                    }
                }
            }

            versionTag.put("removals", removals)
            versionTag.put("additions", additions)
            nbtEntries.put(key.toString(), versionTag)

            ent = map
        }

        tag.put("entries", nbtEntries)
        return tag
    }

    private val JsonPrimitive.asNbtTag: Tag get() = if (isNumber) {
        IntTag(asInt)
    } else if (isString) {
        StringTag(asString)
    } else {
        throw IllegalArgumentException("Unknown primitive type: $this")
    }

}
