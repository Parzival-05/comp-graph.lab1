package behavior.managers

import behavior.CatRole
import behavior.tree.BehaviorNode
import behavior.tree.BehaviorStatus
import behavior.tree.action
import behavior.tree.select
import behavior.tree.sequence
import radar.generators.SeekTargetOffsetGenerator
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.SceneConfig

/**
 * Cat behavior manager for ghost cats.
 *
 * @param cat The ghost cat.
 */
class GhostBehaviorManager(
    private val cat: CatParticle,
) : CatBehaviorManager(cat) {
    val moveTo = { target: CatParticle -> SeekTargetOffsetGenerator<CatParticle>(target.coordinates) }
    var catToPossess: CatParticle? = null

    private fun canPossess(cat: CatParticle) =
        cat.role == CatRole.DEFAULT &&
            cat.state !=
            CatStates.DEAD

    private val moveToClosestCat =
        action {
            catToPossess = cat.nearbyCats.find(::canPossess)
            catToPossess?.let { catToPossess ->
                val offset = moveTo(catToPossess).generate(cat)
                offset.move(cat.coordinates)
                return@action BehaviorStatus.SUCCESS
            }
            return@action BehaviorStatus.FAILURE
        }

    private val tryToPossess =
        action {
            catToPossess?.let { targetCat ->
                if (SceneConfig.metricFunction(cat.coordinates, targetCat.coordinates) < SceneConfig.fightDist &&
                    canPossess(targetCat)
                ) {
                    return@action BehaviorStatus.SUCCESS
                }
            }
            BehaviorStatus.FAILURE
        }

    private val possess =
        action {
            catToPossess?.let { targetCat ->
                if (canPossess(targetCat)) {
                    targetCat.setCatRole(CatRole.POSSESSED)
                    return@action BehaviorStatus.SUCCESS
                }
            }
            BehaviorStatus.FAILURE
        }

    override fun createBehaviorTree(): BehaviorNode {
        val behavior =
            select {
                +sequence {
                    +behavior.tree.repeat(20) {
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
