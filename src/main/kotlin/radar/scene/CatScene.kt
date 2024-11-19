package radar.scene

import CatSimulation.Companion.PARTICLE_COUNT
import core.base.BaseEmitter
import core.base.BaseScene
import radar.collisionDetection.KDTreeCollisionDetection
import radar.logging.logging
import radar.generators.CatGenerator
import radar.generators.MoveGenerator
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

class CatScene(
    override var particles: ArrayList<CatParticle>,
    val sceneConfig: SceneConfig,
    private var catEmitter: BaseEmitter<CatParticle, Point2D, Offset2D> = CatEmitter(particleGenerator = CatGenerator())
) : BaseScene<CatParticle, Point2D, Offset2D, CatCollision, MoveGenerator>(particles) {
    private val lastCollisions = mutableSetOf<CatCollision>()

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
            if (this.calcDistance(
                    collision.particle1.coordinates, collision.particle2.coordinates
                ) > sceneConfig.hissDist
            ) {
                arrayOf(collision.particle1, collision.particle2).forEach {
                    it.setCatState(CatStates.CALM)
                }
            }
        }
        lastCollisions.clear()
    }

    override fun findCollisions(): Array<CatCollision> {
        return KDTreeCollisionDetection.findCollisions(this)
    }

    private fun calcDistance(
        p1: Point2D, p2: Point2D
    ): Float = sceneConfig.metricFunction(p1, p2)


    fun calcNewState(dist: Float): CatStates {
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
