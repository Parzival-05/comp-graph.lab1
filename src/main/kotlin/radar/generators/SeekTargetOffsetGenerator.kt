package radar.generators

import core.base.BaseParticle
import core.base.generators.BaseOffsetGenerator
import radar.scene.CatParticle
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.SceneConfig
import kotlin.math.sqrt

/**
 * Generates movement offsets for [CatParticle] based on the given target point and speed.
 *
 * @param target The target point to approach.
 */
class SeekTargetOffsetGenerator<P : BaseParticle<Point2D, Offset2D>>(
    private val target: Point2D,
) : BaseOffsetGenerator<P, Point2D, Offset2D> {
    override fun generate(particle: P): Offset2D {
        val dx = target.x - particle.coordinates.x
        val dy = target.y - particle.coordinates.y

        // normalize the vector
        val magnitude = sqrt(dx * dx + dy * dy)
        val normalizedX = if (magnitude != 0.0) dx / magnitude else 0.0
        val normalizedY = if (magnitude != 0.0) dy / magnitude else 0.0

        // Scale the normalized vector by the speed
        return Offset2D(normalizedX * SceneConfig.maxParticleSpeed, normalizedY * SceneConfig.maxParticleSpeed)
    }
}
