package io.github.kruchon

import test.package.UserImpl
import io.github.kruchon.Account

class Test {
    @Test
    fun test() {
        val user = UserImpl()
        user.run {
            `log into`(account = Account())
        }
    }
}