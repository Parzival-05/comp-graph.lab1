package radar.metrics

import BaseTest
import org.junit.jupiter.api.Test
import radar.scene.Point2D

class ManhattanTest : BaseTest() {
    @Test
    fun `calculate Manhattan distance between two points in the same quadrant`() {
        val point1 = Point2D(2.0, 3.0)
        val point2 = Point2D(5.0, 7.0)

        val distance = manhattan(point1, point2)

        assertAlmostEqual(7.0, distance)
    }

    @Test
    fun `calculate Manhattan distance between two points in different quadrants`() {
        val point1 = Point2D(-2.0, 3.0)
        val point2 = Point2D(4.0, -1.0)

        val distance = manhattan(point1, point2)

        assertAlmostEqual(10.0, distance)
    }

    @Test
    fun `calculate Manhattan distance when one point is at the origin`() {
        val point1 = Point2D(0.0, 0.0)
        val point2 = Point2D(3.0, 4.0)

        val distance = manhattan(point1, point2)

        assertAlmostEqual(7.0, distance)
    }

    @Test
    fun `calculate Manhattan distance with negative coordinates`() {
        val point1 = Point2D(-3.0, -4.0)
        val point2 = Point2D(-1.0, -2.0)

        val distance = manhattan(point1, point2)

        assertAlmostEqual(4.0, distance)
    }

    @Test
    fun `calculate Manhattan distance with large coordinate values`() {
        val point1 = Point2D(1_000_000_000_000.0, 1_000_000_000_000.0)
        val point2 = Point2D(0.0, 0.0)

        val distance = manhattan(point1, point2)

        assertAlmostEqual(2_000_000_000_000.0, distance)
    }
}
