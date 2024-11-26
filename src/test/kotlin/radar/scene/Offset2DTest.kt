package radar.scene;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Offset2DTest {

    @Test
    fun `test move with positive offset`() {
        val point = Point2D(x = 10.0, y = 20.0)
        val offset = Offset2D(x = 5.0, y = 5.0)
        offset.move(point)
        assertEquals(15f, point.x)
        assertEquals(25f, point.y)
    }

    @Test
    fun `test move with negative offset`() {
        val point = Point2D(x = 10.0, y = 20.0)
        val offset = Offset2D(x = -3.0, y = -7.0)
        offset.move(point)
        assertEquals(7f, point.x)
        assertEquals(13f, point.y)
    }

    @Test
    fun `test move with zero offset`() {
        val point = Point2D(x = 10.0, y = 20.0)
        val offset = Offset2D(x = 0.0, y = 0.0)
        offset.move(point)
        assertEquals(10f, point.x)
        assertEquals(20f, point.y)
    }
}