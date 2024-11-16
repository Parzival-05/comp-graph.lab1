package radar.scene

import GRID_SIZE_X
import GRID_SIZE_Y
import core.base.BaseEmitter
import radar.generators.CatGenerator
import kotlin.random.Random

class CatEmitter(
    private val particleGenerator: CatGenerator
) : BaseEmitter<CatParticle, Point2D, Offset2D> {
    override fun emit(n: Int): Set<CatParticle> {
        val particles = mutableSetOf<CatParticle>()
        for (i in 1..n) {
            val particleCoordinates = if (Random.nextInt(0, 1) == 0) {
                val y = Random.nextInt(GRID_SIZE_Y)
                Point2D(0.0f, y.toFloat())
            } else {
                val x = Random.nextInt(GRID_SIZE_X)
                Point2D(x.toFloat(), 0.0f)
            }
            particles.add(
                particleGenerator.generate(particleCoordinates)
            )
        }
        return particles.toSet()
    }
}
