package core.base.generators

import core.base.BaseOffset
import core.base.BaseParticle
import core.base.BasePoint

/**
 * Defines a generator responsible for creating particles.
 *
 * @param P The type of particles this generator creates.
 * @param T The type of point representing positions within the scene.
 * @param O The type of offset related to the particles.
 */
interface BaseParticleGenerator<P : BaseParticle<T, O>, T : BasePoint<O>, O : BaseOffset<T>> {
    /**
     * Generates a new particle instance.
     *
     * @return A newly created particle.
     */
    fun generate(): P
}
