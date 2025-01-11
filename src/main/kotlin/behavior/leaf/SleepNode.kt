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
    private val repeaterNode = RepeaterNode(ActionNode.success, sleepTime)

    override fun tick(cat: CatParticle): BehaviorStatus = repeaterNode.tick(cat)
}
