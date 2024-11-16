package core.base.generators

import core.base.BaseOffset
import core.base.BaseParticle
import core.base.BasePoint


interface BaseParticleGenerator<P : BaseParticle<T, O>, T : BasePoint<O>, O : BaseOffset<T>> {
    fun generate(): P
}