package radar.scene.behavior

import behavior.BehaviorNode
import behavior.BehaviorStatus
import behavior.action
import behavior.select
import behavior.sequence
import radar.generators.SeekTargetOffsetGenerator
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.SceneConfig
import radar.scene.behavior.gang.CatRole

/**
 * Cat behavior manager for ghost cats.
 *
 * @param cat The ghost cat.
 */
class GhostBehaviorManager(
    private val cat: CatParticle,
) : CatBehaviorManager(cat) {
    val moveTo = { target: CatParticle -> SeekTargetOffsetGenerator<CatParticle>(target.coordinates) }

    val moveToClosestCat =
        action { cat ->
            // todo: better condition
            val closestCat =
                cat.nearbyCats.find { otherCat ->
                    otherCat.role == CatRole.DEFAULT &&
                        otherCat.state !=
                        CatStates.DEAD
                }
            if (closestCat == null) return@action BehaviorStatus.FAILURE
            val offset = moveTo(closestCat).generate(cat)
            offset.move(cat.coordinates)
            BehaviorStatus.SUCCESS
        }
    val tryToPossess =
        action { cat ->
            val closestCat =
                cat.nearbyCats.find { otherCat ->
                    otherCat.role == CatRole.DEFAULT &&
                        otherCat.state !=
                        CatStates.DEAD
                }
            if (closestCat == null) return@action BehaviorStatus.FAILURE
            if (SceneConfig.metricFunction(cat.coordinates, closestCat.coordinates) < SceneConfig.fightDist) {
                BehaviorStatus.SUCCESS
            } else {
                BehaviorStatus.FAILURE
            }
        }
    val possess =
        action { cat ->
            // todo: эх вот бы сюда СТЕЙТ МОНАДУ
            val closestCat =
                cat.nearbyCats.find { otherCat ->
                    otherCat.role == CatRole.DEFAULT &&
                        otherCat.state !=
                        CatStates.DEAD
                }
            if (closestCat == null) return@action BehaviorStatus.FAILURE
            closestCat.setCatRole(CatRole.POSSESSED)
            BehaviorStatus.SUCCESS
        }

    override fun createBehaviorTree(): BehaviorNode {
        val behavior =
            select {
                +sequence {
                    +behavior.repeat(20) {
                        sequence {
                            +moveToClosestCat
                            +tryToPossess
                        }
                    }
                    +possess
                }
                +moveRandomList
            }

        return behavior
    }
}
