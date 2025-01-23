package radar.metrics

import BaseTest
import radar.scene.Point2D
import kotlin.math.sqrt
import kotlin.test.Test

class EuclideanTest : BaseTest() {
    @Test
    fun `calculate the distance between two points in a 2D space`() {
        val point1 = Point2D(3.0, 4.0)
        val point2 = Point2D(0.0, 0.0)

        val distance = euclidean(point1, point2)

        assertAlmostEqual(5.0, distance)
    }

    @Test
    fun `calculate distance with negative coordinates`() {
        val point1 = Point2D(-3.0, -4.0)
        val point2 = Point2D(0.0, 0.0)

        val distance = euclidean(point1, point2)

        assertAlmostEqual(5.0, distance)
    }

    @Test
    fun `calculate distance with large coordinate values`() {
        val point1 = Point2D(1_000_000.0, 1_000_000.0)
        val point2 = Point2D(0.0, 0.0)

        val distance = euclidean(point1, point2)

        assertAlmostEqual(sqrt(2_000_000_000_000.0), distance)
    }
}
