package behavior.composite

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

/**
 * Sequence runs node in sequence until one of them fails.
 *
 * @param children children nodes.
 */
class SequenceNode(override val children: MutableList<BehaviorNode> = mutableListOf()) : Composite() {
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
