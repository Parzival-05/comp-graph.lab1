package radar.scene

import CollisionDetection.Companion.THREAD_COUNT
import core.base.BaseCollisionDetection
import core.base.BaseEmitter
import core.base.BaseScene
import radar.collisionDetection.KDTreeCollisionDetection
import radar.logging.logging
import radar.generators.CatGenerator
import radar.generators.MoveGenerator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.pow
import kotlin.random.Random

class CatScene(
    override var particles: ArrayList<CatParticle>,
    val sceneConfig: SceneConfig,
    private var catEmitter: BaseEmitter<CatParticle, Point2D, Offset2D> = CatEmitter(
        particleGenerator = CatGenerator()
    ),
    private val collisionDetection: BaseCollisionDetection<CatScene, CatParticle, Point2D, Offset2D, CatCollision, MoveGenerator> = KDTreeCollisionDetection(
        workerPool, THREAD_COUNT
    )
) : BaseScene<CatParticle, Point2D, Offset2D, CatCollision, MoveGenerator>(particles) {
    private val lastCollisions = mutableSetOf<CatCollision>()

    companion object {
        val workerPool: ExecutorService by lazy {
            println("inited")
            Executors.newFixedThreadPool(THREAD_COUNT)
        }
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
        val nMax = sceneConfig.particleCount - particles.size
        if (nMax <= 0) return

        val n = Random.nextInt(1, nMax + 1)
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

    public override fun findCollisions(): Array<CatCollision> = collisionDetection.findCollisions(this)


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
