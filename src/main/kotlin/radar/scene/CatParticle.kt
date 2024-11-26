package radar.scene

import core.base.BaseParticle

/**
 * Represents a cat in the simulation with specific coordinates and state.
 *
 * @property coordinates The current coordinates of the `CatParticle`.
 * @property state The current state of the `CatParticle` (default is `CALM`).
 */
data class CatParticle(override var coordinates: Point2D, var state: CatStates = CatStates.CALM) :
    BaseParticle<Point2D, Offset2D>() {

    /**
     * Unique identifier for the `CatParticle`.
     */
    val id = COUNT++

    /**
     * Sets the state of the `CatParticle`.
     *
     * @param state The new state to assign to the `CatParticle`.
     */
    fun setCatState(state: CatStates) {
        this.state = state
    }

    companion object {
        /**
         * Counter to assign unique IDs to `CatParticles`.
         */
        var COUNT = 0
    }
}