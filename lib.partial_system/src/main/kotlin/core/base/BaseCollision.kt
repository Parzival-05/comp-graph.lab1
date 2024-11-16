package core.base

abstract class BaseCollision<P : BaseParticle<T, O>, T : BasePoint<O>, O : BaseOffset<T>>(
    open val particle1: P,
    open val particle2: P,
    open val dist: Float
)