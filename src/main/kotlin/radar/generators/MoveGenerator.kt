package radar.generators

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
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
        fun findSide(v: Float, size: Float): Side? {
            val fraction = 0.02
            return if (v < size * fraction) {
                Side.LEFT
            } else if (v > size * (1 - fraction)) {
                Side.RIGHT
            } else {
                null
            }
        }

        fun generateBySide(side: Side?): Int {
            return when (side) {
                Side.LEFT -> Random.nextInt(-sceneConfig.maxParticleSpeed * 2 / 3, sceneConfig.maxParticleSpeed + 1)
                Side.RIGHT -> Random.nextInt(-sceneConfig.maxParticleSpeed, sceneConfig.maxParticleSpeed * 2 / 3 + 1)
                null -> Random.nextInt(-sceneConfig.maxParticleSpeed, sceneConfig.maxParticleSpeed + 1)
            }
        }

        val xSide = findSide(particle.coordinates.x, GRID_SIZE_X.toFloat())
        val ySide = findSide(particle.coordinates.y, GRID_SIZE_Y.toFloat())

        return Offset2D(generateBySide(xSide), generateBySide(ySide))
    }
}
