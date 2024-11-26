package core.base.generators

import core.base.BaseOffset
import core.base.BaseParticle
import core.base.BasePoint

/**
 * Defines a generator for producing offsets based on the state of a particle.
 *
 * @param P The type of particle the offset generator works with.
 * @param T The type of point representing positions within the scene.
 * @param O The type of offset used to move points.
 */
interface BaseOffsetGenerator<P : BaseParticle<T, O>, T : BasePoint<O>, O : BaseOffset<T>> {
    /**
     * Generates an offset for the given particle.
     *
     * @param particle The particle for which the offset is generated.
     * @return The generated offset.
     */
    fun generate(particle: P): O
}
