package behavior.decorator

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

/**
 * Repeats the child node [repeatCount] times or until it fails.
 *
 * @property child the child node to repeat
 * @property repeatCount the number of times to repeat the child node
 */
class RepeaterNode(
    override val child: BehaviorNode,
    private val repeatCount: Int,
) : Decorator() {
    private var counter = 0

    override fun tick(cat: CatParticle): BehaviorStatus {
        if (counter < repeatCount) {
            val status = child.tick(cat)
            if (status == BehaviorStatus.SUCCESS) {
                counter++
            }
            if (status == BehaviorStatus.FAILURE) {
                counter = 0
                return BehaviorStatus.FAILURE
            }
            return BehaviorStatus.RUNNING
        } else {
            counter = 0
            return BehaviorStatus.SUCCESS
        }
    }
}
