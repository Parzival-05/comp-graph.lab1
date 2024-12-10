package radar.scene.behavior

import behavior.BehaviorNode
import behavior.BehaviorStatus
import behavior.flow.RepeaterNode
import behavior.flow.SelectorNode
import behavior.flow.SequenceNode
import behavior.leaf.ActionNode
import radar.generators.SeekTargetOffsetGenerator
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.SceneConfig
import radar.scene.behavior.gang.CatRole

class GhostBehaviorManager(private val cat: CatParticle) : CatBehaviorManager(cat) {
    // todo: part of speed bug probably
    val moveTo = { target: CatParticle -> SeekTargetOffsetGenerator<CatParticle>(target.coordinates, 1.0) }

    val moveToClosestCat =
        ActionNode { cat ->
            val closestCat = cat.nearbyCats.find { otherCat -> otherCat.state != CatStates.DEAD }
            if (closestCat == null) return@ActionNode BehaviorStatus.FAILURE
            val offset = moveTo(closestCat).generate(cat)
            offset.move(cat.coordinates)
            BehaviorStatus.SUCCESS
        }
    val tryToPossess =
        ActionNode { cat ->
            val closestCat = cat.nearbyCats.find { otherCat -> otherCat.state != CatStates.DEAD }
            if (closestCat == null) return@ActionNode BehaviorStatus.FAILURE
            if (SceneConfig.metricFunction(cat.coordinates, closestCat.coordinates) < SceneConfig.fightDist) {
                BehaviorStatus.SUCCESS
            } else {
                BehaviorStatus.FAILURE
            }
        }
    val possess =
        ActionNode { cat ->
            // todo: эх вот бы сюда СТЕЙТ МОНАДУ
            val closestCat = cat.nearbyCats.find { otherCat -> otherCat.state != CatStates.DEAD }
            if (closestCat == null) return@ActionNode BehaviorStatus.FAILURE
            closestCat.setCatRole(CatRole.POSSESSED)
            BehaviorStatus.SUCCESS
        }

    // TODO: THE ORDER MATTERS !!!!
    override val behaviorTree: BehaviorNode = createBehaviorTree()

    override fun createBehaviorTree(): BehaviorNode {
        val behavior =
            SelectorNode(
                listOf(
                    SequenceNode(
                        listOf(
                            RepeaterNode(
                                SequenceNode(
                                    listOf(
                                        moveToClosestCat,
                                        tryToPossess,
                                    ),
                                ),
                                20,
                            ),
                            possess,
                        ),
                    ),
                    moveRandomList,
                ),
            )
        return behavior
    }
}
