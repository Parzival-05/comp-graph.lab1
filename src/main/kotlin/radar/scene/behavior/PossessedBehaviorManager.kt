package radar.scene.behavior

import behavior.BehaviorNode
import behavior.flow.SequenceNode
import radar.scene.CatParticle

/**
 * Cat behavior manager for possessed cats.
 *
 * @param cat The possessed cat.
 */
class PossessedBehaviorManager(private val cat: CatParticle) : CatBehaviorManager(cat) {
    override val behaviorTree: BehaviorNode = createBehaviorTree()

    override fun createBehaviorTree(): BehaviorNode {
        return SequenceNode(
            listOf(
                moveRandomList,
            ),
        )
    }
}
