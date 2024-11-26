package radar.generators

import core.base.generators.BaseOffsetGenerator
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.CatParticle
import radar.scene.SceneConfig
import kotlin.random.Random

/**
 * Generates movement offsets for `CatParticles` based on the speed defined in the scene configuration.
 *
 * @param sceneConfig The configuration containing the maximum particle speed.
 */
class MoveGenerator(private val sceneConfig: SceneConfig) : BaseOffsetGenerator<CatParticle, Point2D, Offset2D> {

    /**
     * Generates an `Offset2D` for a given `CatParticle`.
     *
     * @param particle The `CatParticle` for which to generate the offset.
     * @return An `Offset2D` representing the movement.
     */
    override fun generate(particle: CatParticle): Offset2D {
        return Offset2D(
            Random.nextDouble(-sceneConfig.maxParticleSpeed, sceneConfig.maxParticleSpeed),
            Random.nextDouble(-sceneConfig.maxParticleSpeed, sceneConfig.maxParticleSpeed)
        )
    }
}
