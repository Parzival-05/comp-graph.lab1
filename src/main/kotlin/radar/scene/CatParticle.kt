package radar.scene

import CatSimulation
import behavior.CatRole
import behavior.managers.BehaviorManagerFactory
import core.base.BaseParticle
import radar.logging.InteractionLogger.logStateChange

/**
 * Represents a cat in the simulation with specific coordinates and state.
 *
 * @property coordinates The current coordinates of the [CatParticle].
 * @property state The current state of the [CatParticle] (default is `CALM`).
 */
data class CatParticle(
    override var coordinates: Point2D,
    var state: CatStates = CatStates.CALM,
    var role: CatRole = CatRole.DEFAULT,
) : BaseParticle<Point2D, Offset2D>() {
    /** Unique identifier for the [CatParticle]. */
    val id = count++

    /** Health points for the [CatParticle] before his state changes to [CatStates.DEAD]. */
    var hp = CatSimulation.HEALTH_POINTS_DEFAULT

    /** Nearby cats that this cat is aware of. */
    var nearbyCats: MutableList<CatParticle> = mutableListOf()

    /** Manages the behavior of this cat. */
    private var behaviorManager = BehaviorManagerFactory.create(role, this)

    override fun tick() = behaviorManager.tick()

    fun setCatRole(newRole: CatRole) {
        role = newRole
        behaviorManager = BehaviorManagerFactory.create(role, this)
    }

    fun setCatState(state: CatStates) {
        this.state = state
        if (state != CatStates.FIGHT && state != CatStates.HISS && state != CatStates.CALM) logStateChange(this)
    }

    companion object {
        /** Counter to assign unique IDs to [CatParticle]. */
        var count = 0
    }
}
