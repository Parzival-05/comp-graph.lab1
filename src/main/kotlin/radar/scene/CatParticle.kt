package radar.scene

import core.base.BaseParticle
import radar.scene.behavior.BehaviorManagerFactory
import radar.scene.behavior.gang.CatRole

/**
 * Represents a cat in the simulation with specific coordinates and state.
 *
 * @property coordinates The current coordinates of the `CatParticle`.
 * @property state The current state of the `CatParticle` (default is `CALM`).
 */
data class CatParticle(
    override var coordinates: Point2D,
    var state: CatStates = CatStates.CALM,
    var role: CatRole = CatRole.DEFAULT
) : BaseParticle<Point2D, Offset2D>() {
    /** Unique identifier for the `CatParticle`. */
    val id = count++

    var hp = 100

    /** Nearby cats that this cat is aware of. */
    var nearbyCats: MutableList<CatParticle> = mutableListOf()

    /** Manages the behavior of this cat. */
    private var behaviorManager = BehaviorManagerFactory.create(role, this)

    override fun tick() =
        behaviorManager.tick()

    fun setCatRole(newRole: CatRole) {
        role = newRole
        behaviorManager = BehaviorManagerFactory.create(role, this)
    }

    fun setCatState(state: CatStates) {
        this.state = state
    }

    companion object {
        /** Counter to assign unique IDs to `CatParticles`. */
        var count = 0
    }
}
