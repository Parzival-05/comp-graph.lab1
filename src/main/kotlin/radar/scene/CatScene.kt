package radar.scene

import CollisionDetection.Companion.THREAD_COUNT
import core.base.BaseCollisionDetection
import core.base.BaseEmitter
import core.base.BaseScene
import radar.collisionDetection.KDTreeCollisionDetection
import radar.generators.CatGenerator
import radar.generators.MoveGenerator
import radar.logging.logging
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.pow
import kotlin.random.Random

/**
 * Manages the simulation scene, including particles, their interactions,
 * and configurations.
 *
 * @property particles The list of `CatParticles` in the scene.
 * @property sceneConfig The configuration settings for the scene.
 * @property catEmitter The emitter used to introduce new `CatParticles`
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
    private val collisionDetection: BaseCollisionDetection<CatScene, CatParticle, Point2D, Offset2D, CatCollision, MoveGenerator> =
        KDTreeCollisionDetection(
            workerPool,
            THREAD_COUNT,
        ),
) : BaseScene<CatParticle, Point2D, Offset2D, CatCollision, MoveGenerator>(particles) {
    /** Stores collisions from the last update for state resetting purposes. */
    private val lastCollisions = mutableSetOf<CatCollision>()

    companion object {
        /** Executor service for running tasks concurrently. */
        val workerPool: ExecutorService by lazy {
            Executors.newFixedThreadPool(THREAD_COUNT)
        }
    }

    /** Initializes the scene by finding and reacting to initial collisions. */
    init {
        this.findAndReactCollisions()
    }

    /**
     * Updates the scene by moving particles, resetting states, and processing
     * collisions.
     *
     * @param offsetGenerator The generator for particle movement offsets.
     */
    override fun updateScene(offsetGenerator: MoveGenerator) {
        this.move(offsetGenerator)
        this.resetStates()
        this.findAndReactCollisions()
    }

    /**
     * Finds collisions and reacts to them, then spawns new `CatParticles` if
     * needed.
     */
    override fun findAndReactCollisions() {
        super.findAndReactCollisions()
        this.spawnCats()
    }

    /**
     * Adds new `CatParticles` to the scene to maintain the desired particle
     * count.
     */
    private fun spawnCats() {
        val nMax = sceneConfig.particleCount - particles.size
        if (nMax <= 0) return

        val n = Random.nextInt(1, nMax + 1)
        val cats = catEmitter.emit(n)
        this.particles.addAll(cats)
    }

    /**
     * Resets the states of particles involved in the last collisions back to
     * `CALM`.
     */
    private fun resetStates() {
        for (collision in lastCollisions) {
            arrayOf(collision.particle1, collision.particle2).forEach {
                it.setCatState(CatStates.CALM)
            }
        }
        lastCollisions.clear()
    }

    /**
     * Finds all collisions in the scene using the collision detection
     * algorithm.
     *
     * @return An array of detected CatCollisions.
     */
    public override fun findCollisions(): Array<CatCollision> = collisionDetection.findCollisions(this)

    /**
     * Determines the new state for particles based on the distance between
     * them.
     *
     * @param dist The distance between two particles.
     * @return The new state (`CALM`, `HISS`, or `FIGHT`) for the particles.
     */
    fun calcNewState(dist: Double): CatStates {
        val newState =
            if (dist < sceneConfig.fightDist) {
                CatStates.FIGHT
            } else if (dist < sceneConfig.hissDist && Random.nextDouble(1.0) < 1 / (dist.pow(2))) {
                CatStates.HISS
            } else {
                CatStates.CALM
            }
        return newState
    }

    /**
     * Reacts to detected collisions by updating particle states and logging
     * interactions.
     *
     * @param collisions An array of collisions to process.
     */
    override fun reactCollisions(collisions: Array<CatCollision>) {
        for (collision in collisions) {
            logging(collision)
            arrayOf(collision.particle1, collision.particle2).forEach {
                if (it.state != CatStates.FIGHT) {
                    it.setCatState(collision.catState)
                }
            }
            lastCollisions.add(collision)
        }
    }
}
