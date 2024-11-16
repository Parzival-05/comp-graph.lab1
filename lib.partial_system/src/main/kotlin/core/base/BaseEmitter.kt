package core.base

/** Base Emitter. */
interface BaseEmitter<P : BaseParticle<T, O>, T : BasePoint<O>, O : BaseOffset<T>> {
    /**
     * Emit particles.
     *
     * @property n count of particles to emit.
     */
    fun emit(n: Int): Set<P>
}