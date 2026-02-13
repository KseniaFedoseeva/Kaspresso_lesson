package org.example.ru.webrelab.lesson04

import kotlin.random.Random

class Inventory() {
    val items = mutableListOf<String>()

    operator fun plus(element: String) {
        items.add(element)
    }

    operator fun get(index: Int): String {
        return items[index]
    }

    operator fun contains(item: String): Boolean {
        return item in items
    }
}

class Toggle(private val enabled: Boolean) {
    operator fun not(): Toggle {
        return Toggle(!enabled)
    }
}

class Price(private val amount: Int) {
    operator fun times(number: Int): Int{
        return amount * number
    }
}

class Step(val number: Int) {

    operator fun rangeTo(other: Step): IntRange {
        return number..other.number
    }

    operator fun contains(range: IntRange): Boolean {
        return number in range
    }
}

operator fun IntRange.contains(step: Step): Boolean {
    return step.number in this
}

class Log() {

    private val entries = mutableListOf<String>()

    operator fun plus(entry: String): Log {
        entries.add(entry)
        return this
    }

    fun print() {
        println(entries.joinToString())
    }
}

class Person(private val name: String) {

    private val phrases = mutableListOf<String>()

    infix fun says(text: String): Person {
        phrases.add(text)
        return this
    }

    infix fun and(text: String): Person {
        check(phrases.isNotEmpty()) { "Начни с says" }
        phrases.add(text)
        return this
    }

    infix fun or(text: String): Person {
        check(phrases.isNotEmpty()) { "Начни с says" }
        phrases[phrases.lastIndex] = selectPhrase(phrases[phrases.lastIndex], text)
        return this
    }

    fun print() {
        println(phrases.joinToString(" "))
    }

    private fun selectPhrase(first: String, second: String): String {
        val random = Random.nextInt(0, 2)
        return if (random == 0) first else second
    }
}

