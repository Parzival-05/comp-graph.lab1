package radar.scene.behavior

import behavior.BehaviorNode
import behavior.BehaviorStatus
import behavior.flow.RepeaterNode
import behavior.flow.SelectorNode
import behavior.flow.SequenceNode
import behavior.leaf.ActionNode
import behavior.leaf.ConditionDecoratorNode
import behavior.leaf.ConditionNode
import radar.scene.CatParticle
import radar.scene.CatStates
import kotlin.random.Random

class SimpleBehaviorManager(private val cat: CatParticle): CatBehaviorManager(cat) {
    val shouldSleep = ConditionNode { _ ->
        val sleepProbability = 10e-4
        Random.nextDouble() < sleepProbability
    }

    val setStateToSleeping = ActionNode { cat ->
        cat.setCatState(CatStates.SLEEPING)
        BehaviorStatus.SUCCESS
    }

    override val behaviorTree: BehaviorNode = createBehaviorTree()

    override fun createBehaviorTree(): BehaviorNode {
        val behavior = SequenceNode(listOf(
            SequenceNode(listOf(
                moveRandomList,
                setStateToCalm
            )),
            SelectorNode(listOf(
                SequenceNode(listOf(
                    shouldFight,
                )),
                SequenceNode(listOf(
                    shouldHiss,
                )),
                SequenceNode(listOf(
                    shouldSleep,
                    setStateToSleeping,
                    // TODO: constant to scene config
                    RepeaterNode(ActionNode.success, 100)
                )),
            ))
        ))

        val becomeGhost = SequenceNode(listOf(
            SequenceNode(listOf(
                shouldBecomeGhost,
                setRoleToGhost,
            )),
        ))

        return ConditionDecoratorNode(
            condition = { cat -> cat.state != CatStates.DEAD },
            trueBranch = behavior,
            falseBranch = becomeGhost,
        )
    }
}
