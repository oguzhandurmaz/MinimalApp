package com.example.minimaapp

import com.example.minimaapp.utils.Utils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val result = Utils.getDifferences(60000,120000)
        println(result)
        assertEquals(4, 2 + 2)
    }
}
