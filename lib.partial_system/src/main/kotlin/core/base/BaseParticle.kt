package core.base

/**
 * Represents a point in the simulation that can interact with offsets and
 * determine its validity.
 *
 * @param O The type of offset compatible with this point.
 */
interface BasePoint<O> {
    /**
     * Determines if the point is still valid or present within the active
     * simulation scene.
     *
     * @return `true` if the point is part of the scene; otherwise, `false`.
     */
    fun isInScene(): Boolean
}

/**
 * Represents an offset operation that describes how to move a point.
 *
 * @param T The type of point it operates on.
 */
interface BaseOffset<T> {
    /**
     * Moves the associated point by applying this offset.
     *
     * @param coordinates The point to move.
     */
    fun move(coordinates: T)
}

/**
 * Defines a particle in the simulation with specific coordinates and
 * behaviors.
 *
 * @param T The type of point representing the particle's position.
 * @param O The type of offset applied to the particle during movement.
 */
abstract class BaseParticle<T : BasePoint<O>, O : BaseOffset<T>> {
    /** The current coordinates of the particle in the scene. */
    open lateinit var coordinates: T

    /**
     * Checks whether the particle is still part of the scene by querying its
     * position.
     *
     * @return `true` if the particle is in the scene; otherwise, `false`.
     */
    fun isInScene(): Boolean = coordinates.isInScene()
}
