package radar.generators

import core.base.BaseParticle
import core.base.generators.BaseOffsetGenerator
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.SceneConfig

class CurvedLinearOffsetGenerator<P : BaseParticle<Point2D, Offset2D>>(
    private val linearVelocityX: Double,
    private val linearVelocityY: Double,
    private val curveFactor: Double = 0.05,
    private val frequency: Double = 1.0,
) : BaseOffsetGenerator<P, Point2D, Offset2D> {
    private var time: Double = 0.0

    override fun generate(particle: P): Offset2D {
        val curvedX = linearVelocityX + curveFactor * Math.sin(2 * Math.PI * frequency * time)
        val curvedY = linearVelocityY + curveFactor * Math.cos(2 * Math.PI * frequency * time)

        // converting to seconds
        time += SceneConfig.tau / 1000

        return Offset2D(curvedX * SceneConfig.maxParticleSpeed, curvedY * SceneConfig.maxParticleSpeed)
    }
}
