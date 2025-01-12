package behavior.decorator

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

/**
 * Inverts the result of the child node.
 *
 * @property child the child node which behaviour is modified by the decorator
 */
class InverterNode(
    override val child: BehaviorNode,
) : Decorator() {
    override fun tick(cat: CatParticle): BehaviorStatus =
        when (child.tick(cat)) {
            BehaviorStatus.SUCCESS -> BehaviorStatus.FAILURE
            BehaviorStatus.FAILURE -> BehaviorStatus.SUCCESS
            BehaviorStatus.RUNNING -> BehaviorStatus.RUNNING
        }
}
