/*
 * Copyright (C) 2012 The Guava Authors
 * Copyright 2016 Michael Rozumyanskiy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.michaelrocks.bimap

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class AbstractBiMapTest {
  @Test
  fun testIdentityKeySetIteratorRemove() {
    val bimap = object : AbstractBiMap<Int, String>(IdentityHashMap(), IdentityHashMap()) {}
    bimap.put(1, "one")
    bimap.put(2, "two")
    bimap.put(3, "three")
    val iterator = bimap.keys.iterator()
    iterator.next()
    iterator.next()
    iterator.remove()
    iterator.next()
    iterator.remove()
    assertEquals(1, bimap.size.toLong())
    assertEquals(1, bimap.inverse.size.toLong())
  }

  @Test
  fun testIdentityEntrySetIteratorRemove() {
    val bimap = object : AbstractBiMap<Int, String>(IdentityHashMap(), IdentityHashMap()) {}
    bimap.put(1, "one")
    bimap.put(2, "two")
    bimap.put(3, "three")
    val iterator = bimap.entries.iterator()
    iterator.next()
    iterator.next()
    iterator.remove()
    iterator.next()
    iterator.remove()
    assertEquals(1, bimap.size)
    assertEquals(1, bimap.inverse.size)
  }
}
