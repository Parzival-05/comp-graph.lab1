package radar.scene

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Point2DTest {
    @Test
    fun `test isInScene when point is within bounds`() {
        val point = Point2D(x = 50.0, y = 50.0)
        assertTrue(point.isInScene())
    }

    @Test
    fun `test isInScene when point is out of bounds negative`() {
        val point = Point2D(x = -1.0, y = -1.0)
        assertFalse(point.isInScene())
    }

    @Test
    fun `test isInScene when point is out of bounds positive`() {
        val point = Point2D(x = GRID_SIZE_X + 1, y = GRID_SIZE_Y + 1)
        assertFalse(point.isInScene())
    }
}
