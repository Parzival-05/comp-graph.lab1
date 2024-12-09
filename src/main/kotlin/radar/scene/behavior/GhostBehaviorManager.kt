package radar.scene.behavior

import behavior.BehaviorNode
import behavior.BehaviorStatus
import behavior.flow.SelectorNode
import behavior.leaf.ActionNode
import radar.generators.SeekTargetOffsetGenerator
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.SceneConfig

class GhostBehaviorManager(private val cat: CatParticle): CatBehaviorManager(cat) {

        // todo: part of speed bug probably
    val moveTo = { target: CatParticle -> SeekTargetOffsetGenerator<CatParticle>(target.coordinates, 1.0) }

    val moveToClosestCat = ActionNode { cat ->
        val closestCat = cat.nearbyCats.find { otherCat ->
            // todo: redundant distance anyway?
            val distance = SceneConfig.metricFunction(cat.coordinates, otherCat.coordinates)
            distance < SceneConfig.hissDist && otherCat.state != CatStates.DEAD
        }
        if (closestCat == null) return@ActionNode BehaviorStatus.FAILURE
        val offset = moveTo(closestCat).generate(cat)
        offset.move(cat.coordinates)
        BehaviorStatus.SUCCESS
    }

    // TODO: THE ORDER MATTERS !!!!
    override val behaviorTree: BehaviorNode = createBehaviorTree()

    override fun createBehaviorTree(): BehaviorNode {
        val behavior = SelectorNode(listOf(
            moveToClosestCat,
            moveRandomList
        ))
        return behavior
    }
}
