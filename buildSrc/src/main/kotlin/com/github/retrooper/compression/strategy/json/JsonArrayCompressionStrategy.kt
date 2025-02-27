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
import com.github.retrooper.compression.asStringList
import com.github.steveice10.opennbt.tag.builtin.ByteTag
import com.github.steveice10.opennbt.tag.builtin.CompoundTag
import com.github.steveice10.opennbt.tag.builtin.IntTag
import com.github.steveice10.opennbt.tag.builtin.ListTag
import com.github.steveice10.opennbt.tag.builtin.StringTag
import com.github.steveice10.opennbt.tag.builtin.Tag
import com.google.gson.JsonElement

object JsonArrayCompressionStrategy : JsonCompressionStrategy() {

    override fun serialize(json: JsonElement): Tag {
        val entries = separateVersions(json.asJsonObject)

        val tag = CompoundTag()
        tag.put("length", ByteTag(entries.size.toByte()))

        // we will remove the first entry, which is the oldest version
        val oldestVersion = entries.pollFirstEntry()

        val nbtEntries = CompoundTag()
        var ent = oldestVersion.value.asJsonArray.asStringList

        nbtEntries.put(oldestVersion.key.toString(), ListTag(ent.map(::StringTag)))

        for ((key, value) in entries) {
            val list = value.asJsonArray.asStringList
            val algo = MeyersDiff.factory().create<String> { a, b -> a == b }
            val diff = DiffUtils.diff(ent, list, algo, null, false)

            val versionTag = CompoundTag()
            val additions = ListTag(CompoundTag::class.java)
            val removals = ListTag(CompoundTag::class.java)
            val changes = ListTag(CompoundTag::class.java)
            for (delta in diff.deltas) {
                when (delta) {
                    is InsertDelta<*> -> {
                        additions.add(CompoundTag().apply {
                            put("pos", IntTag(delta.getSource().position))
                            put("lines", ListTag(delta.getTarget().lines.map(::StringTag)))
                        })
                    }

                    is DeleteDelta<*> -> {
                        removals.add(CompoundTag().apply {
                            put("pos", IntTag(delta.getSource().position))
                            put("size", IntTag(delta.getSource().size()))
                        })
                    }

                    is ChangeDelta<*> -> {
                        changes.add(CompoundTag().apply {
                            put("pos", IntTag(delta.getSource().position))
                            put("size", IntTag(delta.getSource().size()))
                            put("lines", ListTag(delta.getTarget().lines.map(::StringTag)))
                        })
                    }
                }
            }

            versionTag.put("removals", removals)
            versionTag.put("additions", additions)
            versionTag.put("changes", changes)
            nbtEntries.put(key.toString(), versionTag)

            ent = list
        }

        tag.put("entries", nbtEntries)
        return tag
    }

}
