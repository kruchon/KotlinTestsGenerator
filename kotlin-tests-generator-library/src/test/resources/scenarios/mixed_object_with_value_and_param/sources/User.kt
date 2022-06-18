package io.github.kruchon

import io.github.kruchon.Tariff

interface User {
    infix fun pay(tariff: Tariff)
}