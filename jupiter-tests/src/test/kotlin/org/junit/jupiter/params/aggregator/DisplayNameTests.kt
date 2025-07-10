/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */
@file:Suppress("unused")

package org.junit.jupiter.params.aggregator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

// https://github.com/junit-team/junit-framework/issues/1836
object DisplayNameTests {
    @JvmStatic
    fun data() =
        arrayOf(
            arrayOf<Any>("A", 1),
            arrayOf<Any>("B", 2),
            arrayOf<Any>("C", 3),
            arrayOf<Any>("", 4), // empty is okay
            arrayOf<Any?>(null, 5) // null was the problem
        )

    @ParameterizedTest
    @MethodSource("data")
    fun test(
        char: String?,
        number: Int,
        info: TestInfo
    ) {
        assertEquals("[$number] char=$char, number=$number", info.displayName)
    }
}
