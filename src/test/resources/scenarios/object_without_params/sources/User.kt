package io.github.kruchon

import io.github.kruchon.Account

interface User {
    fun `log into`(account: Account)
}