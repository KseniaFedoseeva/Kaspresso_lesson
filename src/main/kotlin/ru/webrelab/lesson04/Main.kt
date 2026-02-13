package org.example.ru.webrelab.lesson04

fun main() {
    val andrew = Person("Andrew")
    andrew says "Hello" and "brothers." or "sisters." and "I believe" and "you" and "can do it" or "can't"
    andrew.print()

    val inventory = Inventory()
    inventory + "Дом"
    inventory + "Слон"
    println(inventory.items)

    val toggle = Toggle(false)
    println(!toggle)

    val price = Price(18)
    println(price * 2)

    val stepFrom = Step(1)
    val stepTo = Step(4)
    val stepBetween = Step(8)
    val range = stepFrom..stepTo
    println(range.joinToString())
    println(stepBetween in range)

    val log = Log()
    log + "w" + "e" + "i" + "d"
    log.print()
}