package behavior.leaf

import behavior.BehaviorNode
import behavior.BehaviorStatus
import radar.scene.CatParticle

/**
 *  Base class for underlying logic of a [CatParticle].
 *
 *  @param action function that accepts [CatParticle] and returns [BehaviorStatus]
 */
class ActionNode(
    private val action: (CatParticle) -> BehaviorStatus,
) : BehaviorNode {
    override fun tick(cat: CatParticle): BehaviorStatus = action(cat)

    companion object {
        val success = ActionNode { BehaviorStatus.SUCCESS }
        val failure = ActionNode { BehaviorStatus.FAILURE }
    }
}
