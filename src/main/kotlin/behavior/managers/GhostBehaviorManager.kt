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

    val moveToClosestCat =
        action {
            catToPossess = cat.nearbyCats.find(::canPossess)
            if (catToPossess == null) return@action BehaviorStatus.FAILURE
            val offset = moveTo(catToPossess!!).generate(cat)
            offset.move(cat.coordinates)
            BehaviorStatus.SUCCESS
        }

    val tryToPossess =
        action {
            if (SceneConfig.metricFunction(cat.coordinates, catToPossess!!.coordinates) < SceneConfig.fightDist &&
                canPossess(catToPossess!!)
            ) {
                BehaviorStatus.SUCCESS
            } else {
                BehaviorStatus.FAILURE
            }
        }
    val possess =
        action {
            if (!canPossess(catToPossess!!)) return@action BehaviorStatus.FAILURE
            catToPossess!!.setCatRole(CatRole.POSSESSED)
            BehaviorStatus.SUCCESS
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
