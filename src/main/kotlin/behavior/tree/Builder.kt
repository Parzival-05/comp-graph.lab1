package behavior.tree

import behavior.tree.composite.SelectorNode
import behavior.tree.composite.SequenceNode
import behavior.tree.decorator.RepeaterNode
import behavior.tree.leaf.ActionNode
import behavior.tree.leaf.ConditionNode
import behavior.tree.leaf.SleepNode
import radar.scene.CatParticle

/*
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
inline fun repeat(
    repeatCount: Int = Int.MAX_VALUE,
    action: () -> BehaviorNode,
) = RepeaterNode(
    action(),
    repeatCount,
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

internal fun <T : BehaviorNode> initNode(
    node: T,
    init: T.() -> Unit,
): T {
    node.init()
    return node
}
