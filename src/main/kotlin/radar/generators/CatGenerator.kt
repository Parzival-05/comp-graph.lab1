package radar.generators

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import core.base.generators.BaseParticleGenerator
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.CatParticle
import kotlin.random.Random

class CatGenerator : BaseParticleGenerator<CatParticle, Point2D, Offset2D> {
    override fun generate(): CatParticle {
        val coordinates =
            Point2D(
                Random.nextDouble(GRID_SIZE_X.toDouble()).toFloat(),
                Random.nextDouble(GRID_SIZE_Y.toDouble()).toFloat()
            )
        return generate(coordinates)
    }

    fun generate(coordinates: Point2D): CatParticle {
        return CatParticle(coordinates)
    }
}
