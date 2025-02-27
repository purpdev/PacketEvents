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

import com.github.retrooper.compression.strategy.json.JsonArrayCompressionStrategy
import com.github.retrooper.compression.strategy.json.JsonBase64DataStrategy
import com.github.retrooper.compression.strategy.json.JsonObjectCompressionStrategy
import com.github.retrooper.compression.strategy.json.JsonRegistryCompressionStrategy
import com.github.retrooper.compression.strategy.json.JsonToNbtStrategy

object JsonToNbtDirStrategy : DirCompressionStrategy(JsonToNbtStrategy)
object JsonArrayCompressionDirStrategy : DirCompressionStrategy(JsonArrayCompressionStrategy)
object JsonObjectCompressionDirStrategy : DirCompressionStrategy(JsonObjectCompressionStrategy)
object JsonBase64DataDirStrategy : DirCompressionStrategy(JsonBase64DataStrategy)
object JsonRegistryCompressionDirStrategy : DirCompressionStrategy(JsonRegistryCompressionStrategy)
