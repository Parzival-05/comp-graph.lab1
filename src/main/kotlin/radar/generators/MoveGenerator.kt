package radar.generators

import core.base.generators.BaseOffsetGenerator
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.CatParticle
import radar.scene.SceneConfig
import kotlin.random.Random

class MoveGenerator(private val sceneConfig: SceneConfig) : BaseOffsetGenerator<CatParticle, Point2D, Offset2D> {
    override fun generate(particle: CatParticle): Offset2D {
        return Offset2D(
            Random.nextDouble(-sceneConfig.maxParticleSpeed, sceneConfig.maxParticleSpeed),
            Random.nextDouble(-sceneConfig.maxParticleSpeed, sceneConfig.maxParticleSpeed)
        )
    }
}
