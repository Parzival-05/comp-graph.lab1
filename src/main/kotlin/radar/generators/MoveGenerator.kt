package radar.generators

import core.base.generators.BaseOffsetGenerator
import radar.scene.CatParticle
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.SceneConfig
import kotlin.random.Random

/**
 * Generates movement offsets for [CatParticle] based on the speed defined in the scene configuration.
 */
class MoveGenerator : BaseOffsetGenerator<CatParticle, Point2D, Offset2D> {
    /**
     * Generates an `Offset2D` for a given [CatParticle].
     *
     * @param particle The [CatParticle] for which to generate the offset.
     * @return An `Offset2D` representing the movement.
     */
    override fun generate(particle: CatParticle): Offset2D =
        Offset2D(
            Random.nextDouble(-SceneConfig.maxParticleSpeed, SceneConfig.maxParticleSpeed),
            Random.nextDouble(-SceneConfig.maxParticleSpeed, SceneConfig.maxParticleSpeed),
        )
}
