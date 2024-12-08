package radar.generators

import core.base.BaseParticle
import core.base.generators.BaseOffsetGenerator
import radar.scene.CatParticle
import radar.scene.Offset2D
import radar.scene.Point2D
import kotlin.random.Random

val GENERATORS = listOf(
    { LinearOffsetGenerator<CatParticle>(
        Random.nextDouble(-1.0, 1.0),
        Random.nextDouble(-1.0, 1.0)
    ) },
    { CircularOffsetGenerator(
        radius = Random.nextDouble(),
        angle = Random.nextDouble()
    ) },
    {
        CurvedLinearOffsetGenerator(
            linearVelocityX = Random.nextDouble(-1.0, 1.0),
            linearVelocityY = Random.nextDouble(-1.0, 1.0),
            curveFactor = Random.nextDouble(),
            frequency = Random.nextDouble()
        )
    }

)


class MovementGeneratorFactory<P : BaseParticle<Point2D, Offset2D>>(
    private val generators: List<() -> BaseOffsetGenerator<P, Point2D, Offset2D>>
) {
    /**
     * Randomly selects a movement generator and creates a new instance.
     */
    fun createRandomGenerator(): BaseOffsetGenerator<P, Point2D, Offset2D> {
        val randomFactory = generators.random()
        return randomFactory()
    }
}
