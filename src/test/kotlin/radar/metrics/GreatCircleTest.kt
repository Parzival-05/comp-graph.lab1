package radar.metrics

import BaseTest
import org.junit.jupiter.api.Test
import radar.scene.Point2D
import kotlin.test.assertTrue

class GreatCircleTest : BaseTest() {
    @Test
    fun `calculate the great circle distance between two points in opposite hemispheres`() {
        val point1 = Point2D(0.0, 90.0)
        val point2 = Point2D(0.0, -90.0)

        val distance = greatCircle(point1, point2)

        assertAlmostEqual(210.3658, distance)
    }

    @Test
    fun `calculate the great circle distance between two points on the same latitude`() {
        val point1 = Point2D(0.0, 0.0)
        val point2 = Point2D(180.0, 0.0)

        val distance = greatCircle(point1, point2)

        assertAlmostEqual(236.8705, distance)
    }

    @Test
    fun `calculate the great circle distance between two points on the same longitude`() {
        val point1 = Point2D(0.0, 0.0)
        val point2 = Point2D(0.0, 90.0)

        val distance = greatCircle(point1, point2)

        assertAlmostEqual(105.1768, distance)
    }

    @Test
    fun `calculate the great circle distance between two points in close proximity`() {
        val point1 = Point2D(0.0, 0.0)
        val point2 = Point2D(0.0001, 0.0001)

        val distance = greatCircle(point1, point2)

        assertTrue(distance < 1.0)
    }

    @Test
    fun `calculate the great circle distance between two points with negative coordinates`() {
        val point1 = Point2D(-10.0, -10.0)
        val point2 = Point2D(-20.0, -20.0)

        val distance = greatCircle(point1, point2)

        assertAlmostEqual(17.5993, distance)
    }

    @Test
    fun `calculate the great circle distance between two points with large coordinates`() {
        val point1 = Point2D(1_000_000_000_000.0, 1_000_000_000_000.0)
        val point2 = Point2D(0.0, 0.0)

        val distance = greatCircle(point1, point2)

        assertAlmostEqual(32896.69264, distance)
    }
}
