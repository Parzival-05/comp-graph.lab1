package radar.scene.behavior

import CatSimulation.Companion.DEATH_TIME
import CatSimulation.Companion.SLEEP_PROBABILITY
import CatSimulation.Companion.SLEEP_TIME
import behavior.BehaviorNode
import behavior.BehaviorStatus
import behavior.action
import behavior.condition
import behavior.leaf.ConditionDecoratorNode
import behavior.select
import behavior.sequence
import behavior.sleep
import radar.scene.CatParticle
import radar.scene.CatStates
import kotlin.random.Random

/**
 * Default cat behavior manager.
 *
 * @param cat The cat particle.
 */
class SimpleBehaviorManager(
    private val cat: CatParticle,
) : CatBehaviorManager(cat) {
    private val shouldSleep =
        condition {
            Random.nextDouble() < SLEEP_PROBABILITY
        }

    private val setStateToSleeping =
        action {
            cat.setCatState(CatStates.SLEEPING)
            BehaviorStatus.SUCCESS
        }

    override fun createBehaviorTree(): BehaviorNode {
        val behavior =
            sequence {
                +sequence {
                    +moveRandomList
                    +setStateToCalm
                }
                +select {
                    +shouldFight
                    +shouldHiss
                    +sequence {
                        +shouldSleep
                        +setStateToSleeping
                        +sleep(SLEEP_TIME)
                    }
                }
            }

        val becomeGhost =
            sequence {
                +sequence {
                    +shouldBecomeGhost
                    +setRoleToGhost
                }
            }

        return ConditionDecoratorNode(
            condition = { cat -> cat.state != CatStates.DEAD },
            trueBranch = behavior,
            falseBranch =
                sequence {
                    +sleep(DEATH_TIME)
                    +becomeGhost
                },
        )
    }
}
