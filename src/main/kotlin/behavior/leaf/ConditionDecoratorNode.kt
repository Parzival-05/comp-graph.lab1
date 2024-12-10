package behavior.leaf

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

class ConditionDecoratorNode(
    private val condition: (CatParticle) -> Boolean,
    private val trueBranch: BehaviorNode,
    private val falseBranch: BehaviorNode,
) : BehaviorNode() {
    override fun tick(cat: CatParticle): BehaviorStatus {
        return if (condition(cat)) {
            trueBranch.tick(cat)
        } else {
            falseBranch.tick(cat)
        }
    }
}
