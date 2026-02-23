import org.hamcrest.Description
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.hamcrest.TypeSafeDiagnosingMatcher
import java.util.concurrent.CompletableFuture.anyOf

enum class Color {
    RED,
    GREEN,
    BLACK,
    BLUE,
    YELLOW,
    WHITE,
}

val shapes = listOf(
    Shape(10f, 3, Color.RED),
    Shape(5f, 4, Color.BLUE),
    Shape(7f, 2, Color.GREEN),
    Shape(0.5f, 1, Color.YELLOW),
    Shape(-3f, 5, Color.BLACK),
    Shape(8f, -2, Color.WHITE),
    Shape(12f, 6, Color.RED),
    Shape(15f, 8, Color.BLUE),
    Shape(20f, 4, Color.GREEN),
    Shape(9f, 5, Color.YELLOW),
    Shape(2f, 3, Color.BLACK),
    Shape(11f, 7, Color.WHITE),
    Shape(6f, 10, Color.RED),
    Shape(3f, 2, Color.BLUE),
    Shape(4f, 1, Color.GREEN),
    Shape(25f, 12, Color.YELLOW),
    Shape(30f, 14, Color.BLACK),
    Shape(35f, 16, Color.WHITE),
    Shape(40f, 18, Color.RED),
    Shape(50f, 20, Color.BLUE)

)

data class Shape(val length: Float, val sides: Int, val color: Color)

class LengthMatcher(private val expectedLengthMin: Float = 0.1F, private val expectedLengthMax: Float = 100.0F)
    : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("shape must has side length from $expectedLengthMin to $expectedLengthMax")
    }

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        if (item.length !in expectedLengthMin..expectedLengthMax) {
            mismatchDescription
                .appendText("length was ")
                .appendValue(item.length)
            return false
        }
        return true
    }
}

class CornerMatcher(private val expectedCorn: Int )
    : TypeSafeDiagnosingMatcher<Shape>(){
    override fun describeTo(description: Description) {
        description.appendText("corner number")
            .appendValue(expectedCorn)
    }

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        val actualCorner = if (item.sides <= 2) 0 else item.sides
        return (expectedCorn == actualCorner).also {
            if (!it) {
                mismatchDescription.appendText("corner number was ")
                    .appendValue(actualCorner)
            }
        }
    }
    }

class NegativeSideLengthMatcher : TypeSafeDiagnosingMatcher<Shape>() {
    override fun describeTo(description: Description) {
        description.appendText("side length is positive")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        return (item.length >= 0).also {
            if (!it) mismatchDescription.appendText("side length was negative")
        }
    }
}

class NegativeSidesMatcher : TypeSafeDiagnosingMatcher<Shape>() {
    override fun describeTo(description: Description) {
        description.appendText("side number is positive")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        return (item.sides >= 0).also {
            if (!it) {
                mismatchDescription.appendText("side number was negative")
            }
        }
    }
}

class EvenSides : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("side number is even")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        return (item.sides % 2 == 0).also {
            if (!it) {
                mismatchDescription.appendText("side number wasn't even")
            }
        }
    }
}

class ColorMatcher(private val expectedColor: Color) : TypeSafeDiagnosingMatcher<Shape>() {
    override fun describeTo(description: Description) {
        description.appendText("shape must has color $expectedColor")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        if (item.color != expectedColor) {
            mismatchDescription
                .appendText("color was ")
            return false
        } else {
            return true
        }
    }
}

fun hasCorners(cornerNumber: Int) = CornerMatcher(cornerNumber)
fun hasValidSides() = NegativeSidesMatcher()
fun hasValidSideLength() = NegativeSideLengthMatcher()
fun hasSideLengthInRange(min: Float, max: Float) = LengthMatcher(min, max)
fun hasEvenSides() = EvenSides()
fun hasColor(expectedColor: Color) = ColorMatcher(expectedColor)

val filteredShapes = shapes.filter { shape ->
    allOf(
        hasSideLengthInRange(1f, 20f),
        hasEvenSides(),
        hasValidSideLength(),
        hasValidSides()
            ).matches(shape)
}

val filteredShapesOfCornersAndColors = shapes.filter { shape ->
    anyOf(
        hasColor(Color.BLUE),
        hasCorners(4)
    ).matches(shape)

}

fun testMatchers() {
    val shape = Shape(10f, 4, Color.RED)
    assertThat(shape, hasSideLengthInRange(1f, 20f))
    assertThat(shape, hasColor(Color.RED))
    assertThat(shape, hasValidSideLength())
    assertThat(shape, hasValidSides())
}

fun testMatchersColorAndCorners() {
    val shape = Shape(40f, 4, Color.BLUE)
    assertThat(shape, hasSideLengthInRange(2f, 40f))
    assertThat(shape, hasColor(Color.BLUE))
    assertThat(shape, hasValidSideLength())
    assertThat(shape, hasCorners(4))
}

fun main() {
    testMatchers()
    println("Фигуры, прошедшие фильтрацию: $filteredShapes")

    testMatchersColorAndCorners()
    println("Фигуры, отфильтрованные по углам и цвету: $filteredShapesOfCornersAndColors")
}