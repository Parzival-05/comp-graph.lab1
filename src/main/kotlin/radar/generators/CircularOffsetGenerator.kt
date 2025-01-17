package radar.generators

import core.base.BaseParticle
import core.base.generators.BaseOffsetGenerator
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.SceneConfig
import kotlin.math.cos
import kotlin.math.sin

/**
 * Generates circular movement offsets for [BaseParticle] based on the given radius and angle.
 *
 * @param radius The radius of the circle.
 */
class CircularOffsetGenerator<P : BaseParticle<Point2D, Offset2D>>(
    private val radius: Double,
    private var angle: Double = 0.0,
) : BaseOffsetGenerator<P, Point2D, Offset2D> {
    override fun generate(particle: P): Offset2D {
        val angularVelocity = SceneConfig.maxParticleSpeed / 100.0

        // Mapping polar coordinates to Cartesian coordinate
        val offsetX = radius * cos(angle)
        val offsetY = radius * sin(angle)

        angle = (angle + angularVelocity) % (2 * Math.PI)

        return Offset2D(offsetX * SceneConfig.maxParticleSpeed, offsetY * SceneConfig.maxParticleSpeed)
    }
}
