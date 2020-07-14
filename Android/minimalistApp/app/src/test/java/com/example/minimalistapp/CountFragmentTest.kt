package com.example.minimalistapp

import org.junit.Assert.*
import org.junit.Test

class CountFragmentTest{

    @Test
    fun calculate_get_result(){
        val result = CountFragment().calculate(5,6)
        assertEquals(result,11)
    }
}