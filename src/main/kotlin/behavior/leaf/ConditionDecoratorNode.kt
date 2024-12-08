package behavior.leaf

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

class ConditionDecoratorNode(
    private val condition: (CatParticle) -> Boolean,
    private val child: BehaviorNode
) : BehaviorNode() {
    override fun tick(cat: CatParticle): BehaviorStatus {
        return if (condition(cat)) {
            child.tick(cat)
        } else {
            BehaviorStatus.FAILURE
        }
    }
}
