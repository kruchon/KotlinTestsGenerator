package io.github.kruchon

import io.github.kruchon.Tariff

interface User {
    fun `pay`(tariff: Tariff)
}