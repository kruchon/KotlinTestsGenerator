package io.github.kruchon

import io.github.kruchon.Card
import io.github.kruchon.Message
import io.github.kruchon.Button

interface User {
    infix fun enter(card: Card)
    infix fun get(message: Message)
    infix fun click(button: Button)
}