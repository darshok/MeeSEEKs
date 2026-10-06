package com.glootie.meeseeks.base

import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Before

abstract class BaseUnitTest {

    @Before
    open fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @After
    open fun tearDown() {
        clearAllMocks()
    }
}
