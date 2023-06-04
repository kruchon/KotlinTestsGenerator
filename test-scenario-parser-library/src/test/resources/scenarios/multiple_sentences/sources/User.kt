package io.github.kruchon

import io.github.kruchon.Cost

interface User {
    infix fun `pay tariff with`(cost: Cost)
}