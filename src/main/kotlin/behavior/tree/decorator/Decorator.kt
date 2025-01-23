package behavior.tree.decorator

import behavior.tree.BehaviorNode
import behavior.tree.BehaviourTreeDslMarker

/**
 * Abstract node class for decorator design pattern.
 *
 * @property child the child node which behaviour is modified by the decorator
 */
@BehaviourTreeDslMarker
sealed class Decorator : BehaviorNode {
    protected abstract val child: BehaviorNode
}
