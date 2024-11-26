package radar.scene

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import core.base.BaseEmitter
import radar.generators.CatGenerator
import kotlin.random.Random

/**
 * Emits `CatParticles` into the scene, typically used to add new particles over time.
 *
 * @param particleGenerator The generator used to create new `CatParticles`.
 */
open class CatEmitter(
    private val particleGenerator: CatGenerator
) : BaseEmitter<CatParticle, Point2D, Offset2D> {
    override fun emit(n: Int): Set<CatParticle> {
        val particles = mutableSetOf<CatParticle>()
        for (i in 1..n) {
            val particleCoordinates = if (Random.nextInt(0, 2) == 0) {
                val y = Random.nextDouble(GRID_SIZE_Y)
                Point2D(0.0, y)
            } else {
                val x = Random.nextDouble(GRID_SIZE_X)
                Point2D(x, 0.0)
            }
            particles.add(
                particleGenerator.generate(particleCoordinates)
            )
        }
        return particles.toSet()
    }
}
