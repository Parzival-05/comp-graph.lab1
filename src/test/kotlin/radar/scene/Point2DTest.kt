package radar.scene;

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Point2DTest {

    @Test
    fun `test isInScene when point is within bounds`() {
        val point = Point2D(x = 50f, y = 50f)
        assertTrue(point.isInScene())
    }

    @Test
    fun `test isInScene when point is out of bounds negative`() {
        val point = Point2D(x = -1f, y = -1f)
        assertFalse(point.isInScene())
    }

    @Test
    fun `test isInScene when point is out of bounds positive`() {
        val point = Point2D(x = GRID_SIZE_X.toFloat() + 1, y = GRID_SIZE_Y.toFloat() + 1)
        assertFalse(point.isInScene())
    }
}