package behavior.managers

import CatSimulation
import CatSimulation.Companion.DEATH_TIME
import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import CatSimulation.Companion.SLEEP_TIME
import behavior.CatRole
import core.base.generators.BaseOffsetGenerator
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import radar.generators.MovementGeneratorFactory
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.SceneConfig
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test

class SimpleBehaviorManagerTest {
    @BeforeEach
    fun setup() {
        mockkObject(MovementGeneratorFactory)
        every {
            MovementGeneratorFactory.createRandomGenerator(any())
        } returns
            object : BaseOffsetGenerator<CatParticle, Point2D, Offset2D> {
                override fun generate(particle: CatParticle): Offset2D {
                    return Offset2D(0.0, 0.0) // Stationary offset
                }
            }
    }

    @AfterTest
    fun end() {
        unmockkAll()
    }

    @Test
    fun `cat transitions to HISS when nearby cat is within hiss distance and probability condition passes`() {
        val cat = CatParticle(coordinates = Point2D(0.0, 0.0))
        val nearbyCat =
            CatParticle(coordinates = Point2D(0.0, 2.0))

        mockkObject(SceneConfig)
        every { SceneConfig.metricFunction(cat.coordinates, any()) } returns (
            SceneConfig.fightDist +
                SceneConfig.hissDist
        ) / 2.0
        cat.nearbyCats.add(nearbyCat)
        mockkObject(Random)
        every { Random.nextDouble() } returns 0.0
        val manager = SimpleBehaviorManager(cat)
        manager.tick()
        assertEquals(CatStates.HISS, cat.state)
    }

    @Test
    fun `cat should fight when nearby cat is at appropriate distance`() {
        val cat = CatParticle(coordinates = Point2D(0.0, 0.0))
        val otherCat = CatParticle(coordinates = Point2D(0.0, 0.0))
        mockkObject(SceneConfig)
        every { SceneConfig.metricFunction(cat.coordinates, any()) } returns SceneConfig.fightDist / 2.0
        cat.nearbyCats.add(otherCat)
        val manager = SimpleBehaviorManager(cat)
        manager.tick()
        assertEquals(CatStates.FIGHT, cat.state)
        assertTrue(cat.hp < CatSimulation.HEALTH_POINTS_DEFAULT)
    }

    @Test
    fun `cat transitions to GHOST role after being dead for DEATH_TIME if probability condition is satisfied`() {
        val cat = CatParticle(coordinates = Point2D(0.0, 0.0))
        cat.state = CatStates.DEAD
        val manager = SimpleBehaviorManager(cat)

        mockkObject(Random)
        every { Random.nextDouble() } returns CatSimulation.GHOST_PROBABILITY / 2 // Probability (< 10e-2)
        repeat(DEATH_TIME) { manager.tick() }

        manager.tick()

        assertEquals(CatRole.GHOST, cat.role)
        assertEquals(CatStates.DEAD, cat.state)
    }

    @Test
    fun `cat transitions to SLEEPING when sleep probability condition is satisfied`() {
        val cat = CatParticle(coordinates = Point2D(0.0, 0.0))
        val manager = SimpleBehaviorManager(cat)
        mockkObject(Random)
        every { Random.nextDouble() } returns CatSimulation.SLEEP_PROBABILITY / 2
        manager.tick()
        assertEquals(CatStates.SLEEPING, cat.state)
        repeat(SLEEP_TIME) { manager.tick() }
        // maintain calm state (otherwise we "fall" into sleep condition again)
        every { Random.nextDouble() } returns 1.0
        manager.tick()
        assertEquals(CatStates.CALM, cat.state)
    }

    @Test
    fun `cat is outside of canvas bottom right and respawns on the border top left`() {
        val cat = CatParticle(coordinates = Point2D(GRID_SIZE_X + 1, GRID_SIZE_Y + 1))
        val manager = SimpleBehaviorManager(cat)
        manager.tick()
        assertEquals(1.0, cat.coordinates.x)
        assertEquals(1.0, cat.coordinates.y)
    }

    @Test
    fun `cat is outside of canvas top left and respawns on the border bottom right`() {
        val cat = CatParticle(coordinates = Point2D(-1.0, -1.0))
        val manager = SimpleBehaviorManager(cat)
        manager.tick()
        assertEquals(GRID_SIZE_X - 1, cat.coordinates.x)
        assertEquals(GRID_SIZE_Y - 1, cat.coordinates.y)
    }
}
