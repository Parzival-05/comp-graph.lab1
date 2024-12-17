package behavior.leaf

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

class ConditionNode(private val condition: (CatParticle) -> Boolean) : BehaviorNode() {
    override fun tick(cat: CatParticle): BehaviorStatus {
        return if (condition(cat)) BehaviorStatus.SUCCESS else BehaviorStatus.FAILURE
    }
}
