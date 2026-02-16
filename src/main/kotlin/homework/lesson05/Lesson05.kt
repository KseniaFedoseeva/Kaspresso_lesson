package homework.lesson05

import homework.lesson05.Lesson05.Stock
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.min

class Lesson05 {
    data class Product(val name: String, val weight: Int, val price: Double)

    class Stock {

        private val products = mutableMapOf<Product, Int>()

        operator fun invoke(fnc: Stock.() -> Unit) {
            fnc()
        }

        fun addProduct(product: Product) {
            products[product] = products.getOrDefault(product, 0) + 1
        }

        fun addProduct(name: String, weight: Int, price: Double) {
            addProduct(Product(name, weight, price))
        }

        fun get(product: Product, amount: Int): Int {
            val currentAmount = products.getOrDefault(product, 0)
            val amountToReturn = min(currentAmount, amount)
            products[product] = currentAmount - amountToReturn
            return amountToReturn
        }

        override fun toString(): String {
            return products.map { (product, quantity) ->
                "${product.name} (${product.weight}g): $${product.price} $quantity items"
            }.joinToString("\n")
                .let { "** Stock **\n$it" }
        }

        infix fun String.weight(weight: Int): Pair<String, Int> {
            return this to weight
        }

        private var times = 1

        operator fun Int.times(name: String): String {
            times = this
            return name
        }

        infix fun Pair<String, Int>.by(price: Double) {
            repeat(times) {
                addProduct(first, second, price)
            }
            times = 1
        }

        infix fun Int.x(name: String): String {
            times = this
            return name
        }

        infix fun String.x(weight: Int): Pair<String, Int> {
            return this to weight
        }

        infix fun Pair<String, Int>.x(price: Double) {
            repeat(times) {
                addProduct(first, second, price)
            }
            times = 1
        }
    }

    data class ScheduleEntity(val lesson: String, val startTime: LocalTime, val endTime: LocalTime)

    enum class Days {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }

    class Schedule {

        private val scheduleOfWeek = mutableMapOf<Days, MutableList<ScheduleEntity>>()
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        fun addSchedule(day: Days, scheduleEntity: ScheduleEntity) {
            scheduleOfWeek.getOrPut(day) { mutableListOf() }.add(scheduleEntity)
        }

        override fun toString(): String {
            return scheduleOfWeek.toSortedMap()
                .map { (day, list) ->
                    list.sortedBy { it.startTime }
                        .joinToString("\n") {
                            "%-15s${it.startTime.format(timeFormatter)} - ${
                                it.endTime.format(
                                    timeFormatter
                                )
                            }".format("\t${it.lesson}:")
                        }.let {
                            "${day.name.lowercase().replaceFirstChar { day.name[0].uppercase() }}:\n$it"
                        }
                }.joinToString("\n\n")
        }

        private var day: Days? = null

        operator fun invoke(fnc: Schedule.() -> Unit) {
            fnc()
        }

        fun monday(fnc: () -> Unit) = addDay(Days.MONDAY, fnc)

        fun tuesday(fnc: () -> Unit) = addDay(Days.TUESDAY, fnc)

        fun wednesday(fnc: () -> Unit) = addDay(Days.WEDNESDAY, fnc)

        fun thursday(fnc: () -> Unit) = addDay(Days.THURSDAY, fnc)

        fun friday(fnc: () -> Unit) = addDay(Days.FRIDAY, fnc)

        fun saturday(fnc: () -> Unit) = addDay(Days.SATURDAY, fnc)

        fun sunday(fnc: () -> Unit) = addDay(Days.SUNDAY, fnc)

        operator fun String.rangeTo(time: String): Pair<LocalTime, LocalTime> {
            return LocalTime.parse(this, timeFormatter) to
                    LocalTime.parse(time, timeFormatter)
        }

        infix fun Pair<LocalTime, LocalTime>.schedule(lesson: String) {
            addSchedule(
                day ?: throw IllegalStateException("Не задан день недели"),
                ScheduleEntity(lesson, first, second)
            )
        }

        private fun addDay(day: Days, fnc: () -> Unit) {
            this.day = day
            fnc()
            this.day = null
        }
    }
}

fun main() {
    val stock = Stock()
    stock {
        3 x "bread" x 200 x 30.0
        "bread" x 250 x 38.0
        "apple" x 1000 x 200.0
    }

    val schedule = Lesson05.Schedule()

    schedule {
        monday {
            "09:00".."11:00" schedule "Math"
        }
        tuesday {
            "09:00".."11:00" schedule "Atr"
        }
        wednesday {
            "09:00".."11:00" schedule "Math"
        }
        thursday {
            "09:00".."11:00" schedule "Engl"
        }
        friday {
            "09:00".."11:00" schedule "History"
        }
    }
}

