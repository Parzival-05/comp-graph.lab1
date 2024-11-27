package core.base

/**
 * Represents a collision between two particles in the simulation.
 *
 * @param P The type of particles involved in collisions.
 * @param T The type of point representing positions within the scene.
 * @param O The type of offset related to the particles.
 * @property particle1 The first particle in the collision.
 * @property particle2 The second particle in the collision.
 * @property dist The distance at which the collision occurred.
 */
abstract class BaseCollision<P : BaseParticle<T, O>, T : BasePoint<O>, O : BaseOffset<T>>(
    open val particle1: P,
    open val particle2: P,
    open val dist: Double,
)
