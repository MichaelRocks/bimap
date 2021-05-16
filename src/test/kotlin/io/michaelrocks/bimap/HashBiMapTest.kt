/*
 * Copyright (C) 2007 The Guava Authors
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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HashBiMapTest {
  @Test
  fun testMapConstructor() {
    /* Test with non-empty Map. */
    val map = mapOf(
      "canada" to "dollar",
      "chile" to "peso",
      "switzerland" to "franc"
    )
    val bimap = HashBiMap.create(map)
    assertEquals("dollar", bimap["canada"])
    assertEquals("canada", bimap.inverse["dollar"])
  }

  @Test
  fun testBashIt() {
    val bimap = HashBiMap<Int, Int>(N)
    val inverse = bimap.inverse

    repeat(N) { i ->
      assertNull(bimap.put(2 * i, 2 * i + 1))
    }
    repeat(N) { i ->
      assertEquals((2 * i + 1).toLong(), (bimap[2 * i] as Int).toLong())
    }
    repeat(N) { i ->
      assertEquals((2 * i).toLong(), (inverse[2 * i + 1] as Int).toLong())
    }
    repeat(N) { i ->
      val oldValue = bimap[2 * i]!!
      assertEquals((2 * i + 1).toLong(), (bimap.put(2 * i, oldValue - 2) as Int).toLong())
    }
    repeat(N) { i ->
      assertEquals((2 * i - 1).toLong(), (bimap[2 * i] as Int).toLong())
    }
    repeat(N) { i ->
      assertEquals((2 * i).toLong(), (inverse[2 * i - 1] as Int).toLong())
    }
    val entries = bimap.entries
    for (entry in entries) {
      entry.setValue(entry.value + 2 * N)
    }
    repeat(N) { i ->
      assertEquals((2 * N + 2 * i - 1).toLong(), (bimap[2 * i] as Int).toLong())
    }
  }

  @Test
  fun testBiMapEntrySetIteratorRemove() {
    val map = HashBiMap<Int, String>()
    map[1] = "one"
    val entries = map.entries
    val iterator = entries.iterator()
    val entry = iterator.next()
    entry.setValue("two") // changes the iterator's current entry value
    assertEquals("two", map[1])
    assertEquals(Integer.valueOf(1), map.inverse["two"])
    iterator.remove() // removes the updated entry
    assertTrue(map.isEmpty())
  }

  @Test
  fun testInsertionOrder() {
    val map = HashBiMap<String, Int>()
    map["foo"] = 1
    map["bar"] = 2
    map["quux"] = 3
    assertContainsExactly(map.entries, "foo" to 1, "bar" to 2, "quux" to 3)
  }

  @Test
  fun testInsertionOrderAfterRemoveFirst() {
    val map = HashBiMap<String, Int>()
    map["foo"] = 1
    map["bar"] = 2
    map["quux"] = 3

    map.remove("foo")
    assertContainsExactly(map.entries, "bar" to 2, "quux" to 3)
  }

  @Test
  fun testInsertionOrderAfterRemoveMiddle() {
    val map = HashBiMap<String, Int>()
    map["foo"] = 1
    map["bar"] = 2
    map["quux"] = 3

    map.remove("bar")
    assertContainsExactly(map.entries, "foo" to 1, "quux" to 3)
  }

  @Test
  fun testInsertionOrderAfterRemoveLast() {
    val map = HashBiMap<String, Int>()
    map["foo"] = 1
    map["bar"] = 2
    map["quux"] = 3

    map.remove("quux")
    assertContainsExactly(map.entries, "foo" to 1, "bar" to 2)
  }

  @Test
  fun testInsertionOrderAfterForcePut() {
    val map = HashBiMap<String, Int>()
    map["foo"] = 1
    map["bar"] = 2
    map["quux"] = 3

    map.forcePut("quux", 1)
    assertContainsExactly(map.entries, "bar" to 2, "quux" to 1)
  }

  @Test
  fun testInsertionOrderAfterInverseForcePut() {
    val map = HashBiMap<String, Int>()
    map["foo"] = 1
    map["bar"] = 2
    map["quux"] = 3

    map.inverse.forcePut(1, "quux")
    assertContainsExactly(map.entries, "bar" to 2, "quux" to 1)
  }

  @Test
  fun testInverseInsertionOrderAfterInverseForcePut() {
    val map = HashBiMap<String, Int>()
    map["foo"] = 1
    map["bar"] = 2
    map["quux"] = 3

    map.inverse.forcePut(1, "quux")
    assertContainsExactly(map.inverse.entries, 2 to "bar", 1 to "quux")
  }

  @Test
  fun testInverseEntrySetValue() {
    val map = HashBiMap<Int, String>()
    map[1] = "one"
    val inverseEntry = map.inverse.entries.first()
    inverseEntry.setValue(2)
    assertEquals(Integer.valueOf(2), inverseEntry.value)
  }

  private fun <K : Any, V : Any> assertContainsExactly(collection: Collection<Map.Entry<K, V>>, vararg pairs: Pair<K, V>) {
    val map = mapOf(*pairs)
    assertEquals(pairs.size, collection.size)
    for ((key, value) in collection) {
      assertEquals(value, map[key])
    }
  }

  companion object {
    private const val N = 1000
  }
}
