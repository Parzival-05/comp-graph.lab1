package radar.generators

import core.base.BaseParticle
import core.base.generators.BaseOffsetGenerator
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.SceneConfig

class CircularOffsetGenerator<P : BaseParticle<Point2D, Offset2D>>(
    private val radius: Double,
    private var angle: Double = 0.0,
    // private val angularVelocity: Double = Math.PI / 60
) : BaseOffsetGenerator<P, Point2D, Offset2D> {
    override fun generate(particle: P): Offset2D {
        val angularVelocity = SceneConfig.maxParticleSpeed / 100

        val offsetX = radius * Math.cos(angle)
        val offsetY = radius * Math.sin(angle)

        angle = (angle + angularVelocity) % (2 * Math.PI)

        return Offset2D(offsetX * SceneConfig.maxParticleSpeed, offsetY * SceneConfig.maxParticleSpeed)
    }
}
