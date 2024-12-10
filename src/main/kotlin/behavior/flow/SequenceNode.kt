package behavior.flow

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

class SequenceNode(private val children: List<BehaviorNode>) : BehaviorNode() {
    private var currentIndex = 0

    override fun tick(cat: CatParticle): BehaviorStatus {
        while (currentIndex < children.size) {
            val status = children[currentIndex].tick(cat)
            if (status == BehaviorStatus.SUCCESS) {
                currentIndex++
            } else if (status == BehaviorStatus.RUNNING) {
                return BehaviorStatus.RUNNING
            } else {
                currentIndex = 0
                return BehaviorStatus.FAILURE
            }
        }
        currentIndex = 0
        return BehaviorStatus.SUCCESS
    }
}
