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

import com.github.steveice10.opennbt.tag.builtin.ByteArrayTag
import com.github.steveice10.opennbt.tag.builtin.ByteTag
import com.github.steveice10.opennbt.tag.builtin.CompoundTag
import com.github.steveice10.opennbt.tag.builtin.Tag
import com.google.gson.JsonElement
import java.util.*

object JsonBase64DataStrategy : JsonCompressionStrategy() {

    override fun serialize(json: JsonElement): Tag {
        val tag = CompoundTag()
        json.asJsonObject.entrySet().forEach {
            val values = CompoundTag()
            it.value.asJsonObject.entrySet().forEach { value ->
                val bytes = Base64.getDecoder().decode(value.value.asString)
                val valueTag = if (bytes.isEmpty()) ByteTag(0) else ByteArrayTag(bytes)
                values.put(value.key, valueTag)
            }
            tag.put(it.key, values)
        }
        return tag
    }
}
