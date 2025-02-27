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

import com.github.steveice10.opennbt.tag.builtin.*
import com.google.gson.JsonElement
import com.google.gson.internal.LazilyParsedNumber

object JsonToNbtStrategy : JsonCompressionStrategy() {

    override fun serialize(json: JsonElement): Tag {
        return json.asNbtTag!!
    }

    private val JsonElement.asNbtTag: Tag? get() = if (isJsonPrimitive) {
        val primitive = asJsonPrimitive
        if (primitive.isBoolean) {
            ByteTag(if (primitive.asBoolean) 1.toByte() else 0.toByte())
        } else if (primitive.isNumber) {
            primitive.asNumber.asNbtTag
        } else if (primitive.isString) {
            StringTag(primitive.asString)
        } else {
            null
        }
    } else if (isJsonArray) {
        ListTag(asJsonArray.mapNotNull { it.asNbtTag })
    } else if (isJsonObject) {
        CompoundTag(asJsonObject.entrySet().mapNotNull { (key, value) -> value.asNbtTag?.let { key to it } }.toMap())
    } else if (isJsonNull) {
        CompoundTag()
    } else {
        null
    }


    private val Number.asNbtTag: Tag get() = when (this) {
        is Double -> DoubleTag(toDouble())
        is Float -> FloatTag(toFloat())
        is Long -> LongTag(toLong())
        is Int, is LazilyParsedNumber -> IntTag(toInt())
        is Short -> ShortTag(toShort())
        is Byte -> ByteTag(toByte())
        else -> throw IllegalArgumentException("Unknown number type: $javaClass")
    }

}
