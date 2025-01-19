package radar.generators

import core.base.BaseParticle
import core.base.generators.BaseOffsetGenerator
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.SceneConfig
import kotlin.math.cos
import kotlin.math.sin

/**
 * Generates curved linear movement offsets for [CatParticle] based on the given velocity and curve factor.
 *
 * This adjustment comes from periodic sinusoidal functions (sine and cosine waves), which create smooth oscillations.
 * These oscillations are applied independently to the X and Y directions of motion, giving the particle's path a
 * soft, curving nature.
 * We could think of it as creating a particle's wobble or zig-zagging motion superimposed over its broader
 * straight-line trajectory.
 * @param linearVelocityX The linear velocity in the x-direction.
 * @param linearVelocityY The linear velocity in the y-direction.
 * @param curveFactor The curve factor for the movement.
 */
class CurvedLinearOffsetGenerator<P : BaseParticle<Point2D, Offset2D>>(
    private val linearVelocityX: Double,
    private val linearVelocityY: Double,
    private val curveFactor: Double = 0.05,
    private val frequency: Double = 1.0,
) : BaseOffsetGenerator<P, Point2D, Offset2D> {
    private var time: Double = 0.0

    override fun generate(particle: P): Offset2D {
        val curvedX = linearVelocityX + curveFactor * sin(2 * Math.PI * frequency * time)
        val curvedY = linearVelocityY + curveFactor * cos(2 * Math.PI * frequency * time)

        // Increment the time value to simulate movement progression. Think of it as an x-axis, while curvedX and
        // curvedY are some functions that depend on it and correspond to y-axis.
        time += SceneConfig.tau / 1000.0

        return Offset2D(curvedX * SceneConfig.maxParticleSpeed, curvedY * SceneConfig.maxParticleSpeed)
    }
}
