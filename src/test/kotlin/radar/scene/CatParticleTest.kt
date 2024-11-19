package radar.scene

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class CatParticleTest {
    @Test
    fun `test default state is CALM`() {
        val cat = CatParticle(coordinates = Point2D(0f, 0f))
        assertEquals(CatStates.CALM, cat.state)
    }

    @Test
    fun `test setting state to HISS`() {
        val cat = CatParticle(coordinates = Point2D(0f, 0f))
        cat.setCatState(CatStates.HISS)
        assertEquals(CatStates.HISS, cat.state)
    }

    @Test
    fun `test unique IDs are assigned`() {
        val cat1 = CatParticle(coordinates = Point2D(0f, 0f))
        val cat2 = CatParticle(coordinates = Point2D(1f, 1f))
        assertNotEquals(cat1.id, cat2.id)
    }
}