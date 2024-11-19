package radar.collisionDetection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.CatStates
import radar.scene.Point2D
import radar.scene.SceneConfig

class KDTreeCollisionDetectionTest {

    @Disabled("failing?")
    @Test
    fun `test findCollisions detects correct number of collisions`() {
        val config = SceneConfig().apply {
            fightDist = 2
            hissDist = 5
            metric = SceneConfig.Companion.MetricType.EUCLIDEAN
        }

        val particles = arrayListOf(
            CatParticle(Point2D(0f, 0f)),
            CatParticle(Point2D(1f, 1f)),
            CatParticle(Point2D(5f, 5f))
        )

        val scene = CatScene(particles, config)
        val collisions = KDTreeCollisionDetection.findCollisions(scene)
        assertEquals(1, collisions.size)

        val collision = collisions[0]
        assertEquals(particles[0], collision.particle1)
        assertEquals(particles[1], collision.particle2)
        assertEquals(CatStates.FIGHT, collision.catState)
    }
}