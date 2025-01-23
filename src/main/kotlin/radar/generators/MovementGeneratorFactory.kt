package radar.generators

import core.base.generators.BaseOffsetGenerator
import radar.scene.CatParticle
import radar.scene.Offset2D
import radar.scene.Point2D
import kotlin.random.Random

val RANDOM_GENERATORS =
    listOf(
        {
            LinearOffsetGenerator<CatParticle>(
                Random.nextDouble(-1.0, 1.0),
                Random.nextDouble(-1.0, 1.0),
            )
        },
        {
            CircularOffsetGenerator(
                radius = Random.nextDouble(),
                angle = Random.nextDouble(),
            )
        },
        {
            CurvedLinearOffsetGenerator(
                linearVelocityX = Random.nextDouble(-1.0, 1.0),
                linearVelocityY = Random.nextDouble(-1.0, 1.0),
                curveFactor = Random.nextDouble(),
                frequency = Random.nextDouble(),
            )
        },
    )

object MovementGeneratorFactory {
    /**
     * Randomly selects a movement generator and creates a new instance.
     */
    fun createRandomGenerator(
        generators: List<() -> BaseOffsetGenerator<CatParticle, Point2D, Offset2D>>,
    ): BaseOffsetGenerator<CatParticle, Point2D, Offset2D> {
        val randomFactory = generators.random()
        return randomFactory()
    }
}
