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
