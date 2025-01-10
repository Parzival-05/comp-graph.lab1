package behavior.leaf

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

/**
 * Behavior node that executes [trueBranch] if condition is true, otherwise [falseBranch].
 *
 * @param condition function that accepts [CatParticle] and returns boolean.
 * @param trueBranch node that will be executed if condition is true.
 * @param falseBranch node that will be executed if condition is false.
 */
class ConditionDecoratorNode(
    private val condition: (CatParticle) -> Boolean,
    private val trueBranch: BehaviorNode,
    private val falseBranch: BehaviorNode,
) : BehaviorNode {
    override fun tick(cat: CatParticle): BehaviorStatus {
        return if (condition(cat)) {
            trueBranch.tick(cat)
        } else {
            falseBranch.tick(cat)
        }
    }
}
