package behavior.leaf

import behavior.BehaviorNode
import behavior.BehaviorStatus
import behavior.decorator.RepeaterNode
import radar.scene.CatParticle

/**
 * Behavior node that does nothing for [sleepTime] amount of ticks.
 *
 * @param sleepTime amount of ticks to sleep.
 */
class SleepNode(val sleepTime: Int) : BehaviorNode {
    override fun tick(cat: CatParticle): BehaviorStatus = RepeaterNode(ActionNode.success, sleepTime).tick(cat)
}
