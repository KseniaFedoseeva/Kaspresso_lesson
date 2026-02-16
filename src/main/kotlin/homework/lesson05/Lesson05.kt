package homework.lesson05

import homework.lesson05.Lesson05.Stock
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
}

fun main() {
    val stock = Stock()
    stock {
        3 x "bread" x 200 x 30.0
        "bread" x 250 x 38.0
        "apple" x 1000 x 200.0
    }
}

