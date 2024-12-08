package radar.scene

import core.base.BaseParticle
import radar.scene.behavior.SimpleBehaviorManager

/**
 * Represents a cat in the simulation with specific coordinates and state.
 *
 * @property coordinates The current coordinates of the `CatParticle`.
 * @property state The current state of the `CatParticle` (default is `CALM`).
 */
data class CatParticle(
    override var coordinates: Point2D,
    var state: CatStates = CatStates.CALM,
) : BaseParticle<Point2D, Offset2D>() {
    /** Unique identifier for the `CatParticle`. */
    val id = count++

    /** Nearby cats that this cat is aware of. */
    var nearbyCats: MutableList<CatParticle> = mutableListOf()

    /** Duration of remaining sleep, if the cat is sleeping. */
    var sleepTicksRemaining: Int = 0

    /** Manages the behavior of this cat. */
    private val behaviorManager = SimpleBehaviorManager(this)

    override fun tick() =
        behaviorManager.tick()

    fun setCatState(state: CatStates) {
        this.state = state
    }

    companion object {
        /** Counter to assign unique IDs to `CatParticles`. */
        var count = 0
    }
}
