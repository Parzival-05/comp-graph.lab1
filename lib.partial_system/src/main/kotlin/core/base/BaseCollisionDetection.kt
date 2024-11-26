package core.base

import core.base.generators.BaseOffsetGenerator

interface BaseCollisionDetection<S : BaseScene<P, T, O, C, OG>, P : BaseParticle<T, O>, T : BasePoint<O>, O : BaseOffset<T>, C : BaseCollision<P, T, O>, OG : BaseOffsetGenerator<P, T, O>> {
    fun findCollisions(scene: S): Array<C>
}