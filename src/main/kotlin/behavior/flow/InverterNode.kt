package behavior.flow

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

class InverterNode(private val child: BehaviorNode) : BehaviorNode() {
    override fun tick(cat: CatParticle): BehaviorStatus {
        return when (child.tick(cat)) {
            BehaviorStatus.SUCCESS -> BehaviorStatus.FAILURE
            BehaviorStatus.FAILURE -> BehaviorStatus.SUCCESS
            BehaviorStatus.RUNNING -> BehaviorStatus.RUNNING
        }
    }
}
