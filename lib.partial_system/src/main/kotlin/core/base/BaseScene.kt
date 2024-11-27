package core.base

import core.base.generators.BaseOffsetGenerator

/**
 * Abstract base class representing the overall simulation or "scene"
 * containing particles.
 *
 * @param P The type of particles contained in the scene.
 * @param T The type of points representing particle positions.
 * @param O The type of offsets applied during movements of particles.
 * @param C The type of collisions detected and handled within the scene.
 * @param OG The type of offset generator used to create offsets for moving
 *     particles.
 * @property particles The list of particles currently present in the
 *     scene.
 */
abstract class BaseScene<
    P : BaseParticle<T, O>,
    T : BasePoint<O>,
    O : BaseOffset<T>,
    C : BaseCollision<P, T, O>,
    OG : BaseOffsetGenerator<P, T, O>,
    >(
    open var particles: ArrayList<P>,
) {
    /**
     * Updates the entire scene by processing movement, detecting collisions,
     * and reacting to those collisions.
     *
     * @param offsetGenerator The offset generator used to calculate particle
     *     movements.
     */
    open fun updateScene(offsetGenerator: OG) {
        this.move(offsetGenerator)
        findAndReactCollisions()
    }

    /** Detects all collisions in the scene and reacts to them appropriately. */
    open fun findAndReactCollisions() {
        val collisions = this.findCollisions()
        this.reactCollisions(collisions)
    }

    /**
     * Moves all particles in the scene using calculated offsets.
     *
     * @param offsetGenerator The offset generator used to compute movements
     *     for particles.
     */
    open fun move(offsetGenerator: OG) {
        this.particles.forEach {
            val offset = offsetGenerator.generate(it)
            offset.move(it.coordinates)
        }
        particles.removeIf { !it.isInScene() }
    }

    /**
     * Finds any particle collisions occurring in the scene.
     *
     * This abstract method must be implemented in subclasses to detect
     * collisions based on custom rules or physics simulations.
     *
     * @return An array of collisions detected in the scene.
     */
    protected abstract fun findCollisions(): Array<C>

    /**
     * Reacts to the detected collisions, modifying the state of particles as
     * needed.
     *
     * This abstract method must be implemented in subclasses to define
     * specific reaction behavior (e.g., resolving overlaps, computing bounce
     * directions).
     *
     * @param collisions The list of collisions that need processing.
     */
    protected abstract fun reactCollisions(collisions: Array<C>)
}
