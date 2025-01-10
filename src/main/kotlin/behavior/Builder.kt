package behavior

import behavior.composite.SelectorNode
import behavior.composite.SequenceNode
import behavior.decorator.RepeaterNode
import behavior.leaf.ActionNode
import behavior.leaf.ConditionNode
import behavior.leaf.SleepNode
import radar.scene.CatParticle

/**
 * DSL
 */

/**
 * @see behavior.leaf.ActionNode
 */
fun action(action: (CatParticle) -> BehaviorStatus) = ActionNode(action)

/**
 * @see behavior.leaf.ConditionNode
 */
fun condition(condition: (CatParticle) -> Boolean) = ConditionNode(condition)

/**
 * @see behavior.decorator.RepeaterNode
 */
fun repeat(repeatCount: Int = Int.MAX_VALUE, action: () -> BehaviorNode) = RepeaterNode(
    action(),
    repeatCount
)

/**
 * @see behavior.composite.SelectorNode
 */
fun select(build: SelectorNode.() -> Unit) = initNode(SelectorNode(), build)

/**
 * @see behavior.composite.SequenceNode
 */
fun sequence(build: SequenceNode.() -> Unit) = initNode(SequenceNode(), build)

/**
 * @see behavior.leaf.SleepNode
 */
fun sleep(sleepTime: Int) = SleepNode(sleepTime)

internal fun <T : BehaviorNode> initNode(node: T, init: T.() -> Unit): T {
    node.init()
    return node
}
