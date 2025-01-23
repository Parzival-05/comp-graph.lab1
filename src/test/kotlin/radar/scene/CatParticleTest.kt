package radar.scene

import BaseTest
import CatSimulation
import behavior.CatRole
import behavior.managers.BehaviorManagerFactory
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import radar.logging.InteractionLogger

class CatParticleTest : BaseTest() {
    @Test
    fun `test default state is CALM`() {
        assertEquals(CatStates.CALM, particle.state)
    }

    @Test
    fun `test setting state to HISS`() {
        particle.setCatState(CatStates.HISS)
        assertEquals(CatStates.HISS, particle.state)
    }

    @Test
    fun `test unique IDs are assigned`() {
        val cat1 = CatParticle(coordinates = Point2D(0.0, 0.0))
        val cat2 = CatParticle(coordinates = Point2D(1.0, 1.0))
        assertNotEquals(cat1.id, cat2.id)
    }

    @Test
    fun `verify that the cat's health points are set to default upon creation`() {
        assertEquals(CatSimulation.HEALTH_POINTS_DEFAULT, particle.hp)
    }

    @Test
    fun `verify that the behavior manager is updated when the cat's role changes`() {
        mockkObject(BehaviorManagerFactory)
        particle.setCatRole(CatRole.GHOST)
        assertTrue(particle.role == CatRole.GHOST)
        verify { BehaviorManagerFactory.create(CatRole.GHOST, particle) }
    }

    @Test
    fun `verify that the cat's state change is logged when it changes to a non-standard state`() {
        mockkObject(InteractionLogger)
        particle.setCatState(CatStates.DEAD)
        verify { InteractionLogger.logStateChange(particle) }
    }
}
