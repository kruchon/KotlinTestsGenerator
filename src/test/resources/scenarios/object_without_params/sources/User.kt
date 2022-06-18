package io.github.kruchon

import io.github.kruchon.Account

interface User {
    infix fun `log into`(account: Account)
}