package radar.scene

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class CatParticleTest {
    @Test
    fun `test default state is CALM`() {
        val cat = CatParticle(coordinates = Point2D(0.0, 0.0))
        assertEquals(CatStates.CALM, cat.state)
    }

    @Test
    fun `test setting state to HISS`() {
        val cat = CatParticle(coordinates = Point2D(0.0, 0.0))
        cat.setCatState(CatStates.HISS)
        assertEquals(CatStates.HISS, cat.state)
    }

    @Test
    fun `test unique IDs are assigned`() {
        val cat1 = CatParticle(coordinates = Point2D(0.0, 0.0))
        val cat2 = CatParticle(coordinates = Point2D(1.0, 1.0))
        assertNotEquals(cat1.id, cat2.id)
    }
}
