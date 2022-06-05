package io.github.kruchon

import io.github.kruchon.Cost

interface User {
    fun `pay tariff with`(cost: Cost)
}