package behavior.flow

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

class SelectorNode(private val children: List<BehaviorNode>) : BehaviorNode() {
    private var currentIndex = 0

    override fun tick(cat: CatParticle): BehaviorStatus {
        while (currentIndex < children.size) {
            val status = children[currentIndex].tick(cat)
            if (status == BehaviorStatus.SUCCESS) {
                currentIndex = 0
                return BehaviorStatus.SUCCESS
            } else if (status == BehaviorStatus.RUNNING) {
                return BehaviorStatus.RUNNING
            } else {
                currentIndex++
            }
        }
        // All children failed
        currentIndex = 0
        return BehaviorStatus.FAILURE
    }
}
