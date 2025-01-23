package behavior.tree.leaf

import behavior.tree.BehaviorNode
import behavior.tree.BehaviorStatus
import behavior.tree.decorator.RepeaterNode
import radar.scene.CatParticle

/**
 * Behavior node that does nothing for [sleepTime] amount of ticks.
 *
 * @param sleepTime amount of ticks to sleep.
 */
class SleepNode(
    val sleepTime: Int,
) : BehaviorNode {
    private val repeaterNode = RepeaterNode(ActionNode.success, sleepTime)

    override fun tick(cat: CatParticle): BehaviorStatus = repeaterNode.tick(cat)
}
