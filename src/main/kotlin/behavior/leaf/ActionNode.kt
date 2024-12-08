package behavior.leaf

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

class ActionNode(private val action: (CatParticle) -> BehaviorStatus) : BehaviorNode() {
    override fun tick(cat: CatParticle): BehaviorStatus = action(cat)
}
