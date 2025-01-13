package radar.scene.behavior

import behavior.BehaviorNode
import behavior.sequence
import radar.scene.CatParticle

/**
 * Cat behavior manager for possessed cats.
 *
 * @param cat The possessed cat.
 */
class PossessedBehaviorManager(
    private val cat: CatParticle,
) : CatBehaviorManager(cat) {
    override fun createBehaviorTree(): BehaviorNode =
        sequence {
            +moveRandomList
        }
}
