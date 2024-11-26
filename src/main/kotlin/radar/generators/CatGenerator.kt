package radar.generators

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import core.base.generators.BaseParticleGenerator
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.CatParticle
import kotlin.random.Random

/**
 * Generator for creating `CatParticle` instances, with the ability to generate at random or specific coordinates.
 */
class CatGenerator : BaseParticleGenerator<CatParticle, Point2D, Offset2D> {
    /**
     * Generates a `CatParticle` with random coordinates within the grid.
     *
     * @return A new `CatParticle` with random coordinates.
     */
    override fun generate(): CatParticle {
        val coordinates =
            Point2D(
                Random.nextDouble(GRID_SIZE_X),
                Random.nextDouble(GRID_SIZE_Y)
            )
        return generate(coordinates)
    }

    /**
     * Generates a `CatParticle` at specified coordinates.
     *
     * @param coordinates The coordinates where the `CatParticle` will be placed.
     * @return A new `CatParticle` at the given coordinates.
     */
    fun generate(coordinates: Point2D): CatParticle {
        return CatParticle(coordinates)
    }
}
