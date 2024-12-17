package radar.scene.behavior

import behavior.BehaviorNode
import behavior.flow.SequenceNode
import radar.scene.CatParticle

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
