package behavior.leaf

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

/**
 * Behavior node that executes [condition] function and returns [BehaviorStatus.SUCCESS] if it returns true,
 * otherwise [BehaviorStatus.FAILURE].
 *
 * @param condition function that accepts [CatParticle] and returns boolean.
 */
class ConditionNode(private val condition: (CatParticle) -> Boolean) : BehaviorNode {
    override fun tick(cat: CatParticle): BehaviorStatus {
        return if (condition(cat)) BehaviorStatus.SUCCESS else BehaviorStatus.FAILURE
    }
}
