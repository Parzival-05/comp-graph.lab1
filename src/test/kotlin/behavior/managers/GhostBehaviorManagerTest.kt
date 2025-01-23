package behavior.managers

import CatSimulation.Companion.POSSESS_TIME
import behavior.CatRole
import core.base.generators.BaseOffsetGenerator
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.BeforeEach
import radar.generators.MovementGeneratorFactory
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.Offset2D
import radar.scene.Point2D
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GhostBehaviorManagerTest {
    @BeforeEach
    fun setup() {
        mockkObject(MovementGeneratorFactory)
        every {
            MovementGeneratorFactory.createRandomGenerator(any())
        } returns
            object : BaseOffsetGenerator<CatParticle, Point2D, Offset2D> {
                override fun generate(particle: CatParticle): Offset2D = Offset2D(0.0, 0.0)
            }
    }

    @AfterTest
    fun end() {
        unmockkAll()
    }

    @Test
    fun `ghost possesses a valid cat successfully`() {
        val ghostCat = CatParticle(coordinates = Point2D(0.0, 0.0), role = CatRole.GHOST)
        val targetCat = CatParticle(coordinates = Point2D(0.0, 0.0), role = CatRole.DEFAULT, state = CatStates.CALM)
        ghostCat.nearbyCats.add(targetCat)

        val manager = GhostBehaviorManager(ghostCat)
        repeat(POSSESS_TIME) { manager.tick() }
        // account for possess itself
        manager.tick()
        assertEquals(CatRole.POSSESSED, targetCat.role)
    }
}
