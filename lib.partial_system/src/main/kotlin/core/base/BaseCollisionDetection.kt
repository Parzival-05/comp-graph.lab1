package core.base

import core.base.generators.BaseOffsetGenerator

/**
 * Interface defining a general collision detection system for a simulation scene.
 *
 * @param S The type of scene that this collision detection operates on.
 * @param P The type of particles contained within the scene.
 * @param T The type of point representing positions within the scene.
 * @param O The type of offset used to move points, extending.
 * @param C The type of collisions detected and handled within the scene, extending.
 * @param OG The type of offset generator used to create offsets for moving particles.
 */
interface BaseCollisionDetection<S : BaseScene<P, T, O, C, OG>, P : BaseParticle<T, O>, T : BasePoint<O>, O : BaseOffset<T>, C : BaseCollision<P, T, O>, OG : BaseOffsetGenerator<P, T, O>> {

    /**
     * Finds all collisions within the given scene.
     *
     * Implementations of this method should analyze the positions of particles within the scene
     * and determine where collisions occur based on defined criteria (e.g., overlapping positions,
     * proximity within a certain threshold).
     *
     * @param scene The simulation scene in which to detect collisions.
     * @return An array of detected collisions, each represented by an instance of [C].
     */
    fun findCollisions(scene: S): Array<C>
}