package radar.generators

import GRID_SIZE_X
import GRID_SIZE_Y
import MAX_PARTICLE_RADIUS
import MIN_PARTICLE_RADIUS
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
        val radius = if (MIN_PARTICLE_RADIUS != MAX_PARTICLE_RADIUS) {
            Random.nextInt(MIN_PARTICLE_RADIUS, MAX_PARTICLE_RADIUS)
        } else {
            MIN_PARTICLE_RADIUS
        }
        return CatParticle(coordinates, radius)
    }
}
