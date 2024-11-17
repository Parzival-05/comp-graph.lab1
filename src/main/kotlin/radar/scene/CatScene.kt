package radar.scene

import CAT_RADIUS
import PARTICLE_COUNT
import core.base.BaseEmitter
import core.base.BaseScene
import radar.generators.CatGenerator
import radar.generators.MoveGenerator
import radar.metrics.euclidean
import radar.metrics.manhattan
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

class CatScene(
    override var particles: ArrayList<CatParticle>, // TODO: use hashset for better search asymptotic (?)
    private val sceneConfig: SceneConfig,
    private var catEmitter: BaseEmitter<CatParticle, Point2D, Offset2D> = CatEmitter(particleGenerator = CatGenerator())
) : BaseScene<CatParticle, Point2D, Offset2D, CatCollision, MoveGenerator>(particles) {
    private val lastCollisions = mutableSetOf<CatCollision>()

    init {
        this.findAndReactCollisions()
    }


    override fun updateScene(offsetGenerator: MoveGenerator) {
        // TODO: reuse code
        this.move(offsetGenerator)
        this.changeStateOfCalmedCats()
        this.findAndReactCollisions()
    }

    override fun findAndReactCollisions() {
        super.findAndReactCollisions()
        this.spawnCats()
    }

    private fun spawnCats() {
        val nMax = PARTICLE_COUNT - particles.size
        val n = (nMax * Random.nextFloat()).roundToInt()
        val cats = catEmitter.emit(n)
        this.particles.addAll(cats)
    }

    private fun changeStateOfCalmedCats() {
        // inefficient and probably incorrect?
        for (collision in lastCollisions) {
            if (this.calcNewState(this.calcDistance(collision.particle1, collision.particle2)) == CatStates.CALM) {
                arrayOf(collision.particle1, collision.particle2).forEach {
                    it.setCatState(CatStates.CALM)
                }
            }
        }
        lastCollisions.clear()
    }

    override fun findCollisions(): Array<CatCollision> {
        // TODO: more efficient collision detection
        val particlesArray = particles.toTypedArray()
        val collided = mutableListOf<CatCollision>()
        for (i in particles.indices) {
            for (j in (i + 1)..<particles.size) {
                val dist = calcDistance((particlesArray[i]), (particlesArray[j]))
                if (dist < sceneConfig.hissDist) {
                    collided.add(CatCollision(particlesArray[i], particlesArray[j], dist))
                }
            }
        }
        return collided.toTypedArray()
    }

    private fun calcDistance(
        cat1: CatParticle, cat2: CatParticle
    ): Float {
        val dist = when (sceneConfig.metric) {
            // TODO: how to remove copy-paste?
            SceneConfig.Companion.MetricType.EUCLIDEAN -> euclidean(
                cat1.coordinates, cat2.coordinates
            )

            SceneConfig.Companion.MetricType.MANHATTAN -> manhattan(cat1.coordinates, cat2.coordinates)
            else -> TODO("other metrics")
        }
        return dist - 2 * CAT_RADIUS
    }

    private fun calcNewState(dist: Float): CatStates {
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
            val newState = calcNewState(collision.dist)
            arrayOf(collision.particle1, collision.particle2).forEach {
                it.setCatState(newState)
            }
            if (newState != CatStates.CALM) {
                lastCollisions.add(collision)
            }
        }
    }
}
