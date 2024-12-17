package core.base

/**
 * Abstract base class representing the overall simulation or "scene"
 * containing particles.
 *
 * @param P The type of particles contained in the scene.
 * @param T The type of points representing particle positions.
 * @param O The type of offsets applied during movements of particles.
 * @param C The type of collisions detected and handled within the scene.
 * @property particles The list of particles currently present in the
 *     scene.
 */
abstract class BaseScene<
    P : BaseParticle<T, O>,
    T : BasePoint<O>,
    O : BaseOffset<T>,
    C : BaseCollision<P, T, O>,
    >(
    open var particles: ArrayList<P>,
) {
    /**
     * Updates the entire scene.
     */

    open fun updateScene() {
        this.particles.forEach { particle ->
            particle.tick()
        }

        // particles.removeIf { !it.isInScene() }
    }

    /** Detects all collisions in the scene and reacts to them appropriately. */
    open fun findAndReactCollisions() {
        val collisions = this.findCollisions()
        this.reactCollisions(collisions)
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
