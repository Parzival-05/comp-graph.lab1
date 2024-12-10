package radar.scene

import core.base.BaseCollision

/**
 * Represents a collision between two `CatParticles`.
 *
 * @property particle1 The first particle involved in the collision.
 * @property particle2 The second particle involved in the collision.
 * @property dist The distance between the particles at the time of
 *     collision.
 */
data class CatCollision(
    override val particle1: CatParticle,
    override val particle2: CatParticle,
    override val dist: Double,
) : BaseCollision<CatParticle, Point2D, Offset2D>(particle1, particle2, dist)
