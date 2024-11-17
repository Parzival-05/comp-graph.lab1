package radar.generators

import GRID_SIZE_X
import GRID_SIZE_Y
import core.base.generators.BaseOffsetGenerator
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.CatParticle
import radar.scene.SceneConfig
import kotlin.random.Random

class MoveGenerator(private val sceneConfig: SceneConfig) : BaseOffsetGenerator<CatParticle, Point2D, Offset2D> {
    companion object {
        enum class Side { LEFT, RIGHT }
    }

    override fun generate(particle: CatParticle): Offset2D {
        // загоняем в поле
        val xSide = if (particle.coordinates.x < GRID_SIZE_X / 10) {
            Side.LEFT
        } else if ((particle.coordinates.x + GRID_SIZE_X / 10) > GRID_SIZE_X) {
            Side.RIGHT
        } else {
            null
        }
        val ySide = if (particle.coordinates.y < GRID_SIZE_Y / 10) {
            Side.LEFT
        } else if ((particle.coordinates.y + GRID_SIZE_Y / 10) > GRID_SIZE_Y) {
            Side.RIGHT
        } else {
            null
        }

        fun generateBySide(side: Side?): Int {
            return when (side) {
                Side.LEFT -> Random.nextInt(-sceneConfig.maxParticleSpeed*1/2, sceneConfig.maxParticleSpeed)
                Side.RIGHT -> Random.nextInt(-sceneConfig.maxParticleSpeed, sceneConfig.maxParticleSpeed*1/2)
                null -> Random.nextInt(-sceneConfig.maxParticleSpeed, sceneConfig.maxParticleSpeed)
            }
        }
        return Offset2D(generateBySide(xSide), generateBySide(ySide))
    }
}
