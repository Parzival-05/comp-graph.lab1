package radar.scene

import core.base.BaseCollision

data class CatCollision(
    override val particle1: CatParticle,
    override val particle2: CatParticle,
    override val dist: Float,
    val catState: CatStates
) : BaseCollision<CatParticle, Point2D, Offset2D>(particle1, particle2, dist)
