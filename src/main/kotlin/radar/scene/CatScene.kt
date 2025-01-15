package radar.scene

import CollisionDetection.Companion.THREAD_COUNT
import core.base.BaseCollisionDetection
import core.base.BaseEmitter
import core.base.BaseScene
import radar.collisionDetection.KDTreeCollisionDetection
import radar.generators.CatGenerator
import radar.scene.SceneConfig.particleCount
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Manages the simulation scene, including particles, their interactions,
 * and configurations.
 *
 * @property particles The list of [CatParticle] in the scene.
 * @property sceneConfig The configuration settings for the scene.
 * @property catEmitter The emitter used to introduce new [CatParticle]
 *     into the scene.
 * @property collisionDetection The mechanism for detecting and handling
 *     collisions.
 */
class CatScene(
    override var particles: ArrayList<CatParticle>,
    val sceneConfig: SceneConfig,
    private var catEmitter: BaseEmitter<CatParticle, Point2D, Offset2D> =
        CatEmitter(
            particleGenerator = CatGenerator(),
        ),
    private val collisionDetection: BaseCollisionDetection<CatScene, CatParticle, Point2D, Offset2D, CatCollision> =
        KDTreeCollisionDetection(
            workerPool,
            THREAD_COUNT,
        ),
) : BaseScene<CatParticle, Point2D, Offset2D, CatCollision>(particles) {
    /** Stores collisions from the last update for state resetting purposes. */
    private val lastCollisions = mutableSetOf<CatCollision>()
    private var ticksUntilUpdate = 0

    companion object {
        /** Executor service for running tasks concurrently. */
        val workerPool: ExecutorService by lazy {
            Executors.newFixedThreadPool(THREAD_COUNT)
        }
    }

    /** Initializes the scene by spawning cats. */
    init {
        this.spawnCats()
    }

    /**
     * Updates the scene by moving particles, resetting states, and processing
     * collisions.
     *
     */
    override fun updateScene() {
        if (ticksUntilUpdate == 30) {
            super.updateScene()
            ticksUntilUpdate = 0
        }
        ticksUntilUpdate++
        this.particles.forEach { particle ->
            particle.tick()
        }
    }

    /**
     * Adds new [CatParticle] to the scene to maintain the desired particle
     * count.
     */
    private fun spawnCats() {
        val cats = catEmitter.emit(particleCount)
        this.particles.addAll(cats)
    }

    /**
     * Finds all collisions in the scene using the collision detection
     * algorithm.
     *
     * @return An array of detected [CatCollision].
     */
    public override fun findCollisions(): Array<CatCollision> = collisionDetection.findCollisions(this)

    override fun reactCollisions(collisions: Array<CatCollision>) {
        particles.forEach { it.nearbyCats.clear() }

        for (collision in collisions) {
            collision.particle1.nearbyCats += collision.particle2
            collision.particle2.nearbyCats += collision.particle1
            lastCollisions.add(collision)
        }
    }
}
