package io.github.kruchon

import test.package.UserImpl
import io.github.kruchon.Tariff
import io.github.kruchon.Price

class Test {
    @Test
    fun test() {
        val user = UserImpl()
        user.run {
            `pay`(tariff = Tariff(price = Price(value = "500"),value = "enterprise"))
        }
    }
}