package behavior.composite

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

/**
 * Selector runs node in sequence until one of them succeeds.
 *
 * @param children children nodes.
 */
class SelectorNode(
    override val children: MutableList<BehaviorNode> = mutableListOf(),
) : Composite() {
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
