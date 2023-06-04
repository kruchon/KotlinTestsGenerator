package io.github.kruchon

import test.package.UserImpl
import test.package.ManagerImpl
import io.github.kruchon.Card
import io.github.kruchon.Button
import io.github.kruchon.Notification
import io.github.kruchon.Message
import io.github.kruchon.Payment
import io.github.kruchon.Cost

class Test {
    @Test
    fun test() {
        val user = UserImpl()
        val manager = ManagerImpl()
        user enter Card(value = "5555555555554444")
        user click Button(value = "pay")
        user click Button(value = "accept")
        manager receive Notification(value = "payment")
        user get Message(payment = Payment(cost = Cost(value = "500"), value = "one"))
    }
}