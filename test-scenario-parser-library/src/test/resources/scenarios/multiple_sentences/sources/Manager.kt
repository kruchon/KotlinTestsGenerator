package io.github.kruchon

import io.github.kruchon.Notification

interface Manager {
    infix fun receive(notification: Notification)
}