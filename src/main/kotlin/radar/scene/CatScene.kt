package radar.scene

import CatSimulation.Companion.PARTICLE_COUNT
import CollisionDetection.Companion.THREAD_COUNT
import core.base.BaseEmitter
import core.base.BaseScene
import radar.collisionDetection.KDTreeCollisionDetection
import radar.logging.logging
import radar.generators.CatGenerator
import radar.generators.MoveGenerator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

class CatScene(
    override var particles: ArrayList<CatParticle>,
    val sceneConfig: SceneConfig,
    private var catEmitter: BaseEmitter<CatParticle, Point2D, Offset2D> = CatEmitter(particleGenerator = CatGenerator())
) : BaseScene<CatParticle, Point2D, Offset2D, CatCollision, MoveGenerator>(particles) {
    private val lastCollisions = mutableSetOf<CatCollision>()
    private val collisionDetection = KDTreeCollisionDetection(workerPool, THREAD_COUNT)

    companion object {
        val workerPool: ExecutorService = Executors.newFixedThreadPool(THREAD_COUNT)
    }

    init {
        this.findAndReactCollisions()
    }

    override fun updateScene(offsetGenerator: MoveGenerator) {
        this.move(offsetGenerator)
        this.resetStates()
        this.findAndReactCollisions()
    }

    override fun findAndReactCollisions() {
        super.findAndReactCollisions()
        this.spawnCats()
    }

    private fun spawnCats() {
        val nMax = PARTICLE_COUNT - particles.size
        val n = ((nMax - 1) * Random.nextFloat()).roundToInt()
        val cats = catEmitter.emit(n)
        this.particles.addAll(cats)
    }

    private fun resetStates() {
        for (collision in lastCollisions) {
            arrayOf(collision.particle1, collision.particle2).forEach {
                it.setCatState(CatStates.CALM)
            }
        }
        lastCollisions.clear()
    }

    override fun findCollisions(): Array<CatCollision> {
        return collisionDetection.findCollisions(this)
    }

    fun calcNewState(dist: Double): CatStates {
        val newState = if (dist < sceneConfig.fightDist) {
            CatStates.FIGHT
        } else if (dist < sceneConfig.hissDist && Random.nextDouble(1.0) < 1 / (dist.pow(2))) {
            CatStates.HISS
        } else {
            CatStates.CALM
        }
        return newState
    }


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
