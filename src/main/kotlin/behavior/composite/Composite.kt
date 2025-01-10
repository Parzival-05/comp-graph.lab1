package behavior.composite

import behavior.BehaviorNode
import behavior.BehaviourTreeDslMarker

/**
 * Abstract node class for a composing different nodes.
 * Composite nodes are used to process one or more children in a sequence depending on the implementation.
 *
 */
@BehaviourTreeDslMarker
sealed class Composite : BehaviorNode {
    protected abstract val children: MutableList<BehaviorNode>

    operator fun BehaviorNode.unaryPlus() {
        children += this
    }

    operator fun BehaviorNode.unaryMinus() {
        children -= this
    }
}
