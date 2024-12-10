package radar.generators

import core.base.BaseParticle
import core.base.generators.BaseOffsetGenerator
import radar.scene.Offset2D
import radar.scene.Point2D

class LinearOffsetGenerator<P : BaseParticle<Point2D, Offset2D>>(
    private val velocityX: Double,
    private val velocityY: Double
) : BaseOffsetGenerator<P, Point2D, Offset2D> {

    override fun generate(particle: P): Offset2D {
        return Offset2D(velocityX, velocityY)
    }
}
