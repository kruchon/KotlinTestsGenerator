package io.github.kruchon

import test.package.UserImpl
import io.github.kruchon.Cost

class Test {
    @Test
    fun test() {
        val user = UserImpl()
        user.run {
            `pay tariff with`(cost = Cost(value = "500"))
        }
    }
}