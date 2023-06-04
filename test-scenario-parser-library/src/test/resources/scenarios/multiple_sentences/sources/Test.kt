package io.github.kruchon

import test.package.UserImpl
import io.github.kruchon.Cost

class Test {
    @Test
    fun test() {
        val user = UserImpl()
        user `pay tariff with` Cost(value = "500")
    }
}