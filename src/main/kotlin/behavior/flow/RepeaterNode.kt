package behavior.flow

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

class RepeaterNode(
    private val child: BehaviorNode,
    private val repeatCount: Int = Int.MAX_VALUE
) : BehaviorNode() {
    private var counter = 0

    override fun tick(cat: CatParticle): BehaviorStatus {
        if (counter < repeatCount) {
            val status = child.tick(cat)
            if (status == BehaviorStatus.SUCCESS || status == BehaviorStatus.FAILURE) {
                counter++
            }
            return BehaviorStatus.RUNNING
        } else {
            counter = 0
            return BehaviorStatus.SUCCESS
        }
    }
}
