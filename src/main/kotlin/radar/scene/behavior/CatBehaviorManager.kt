package radar.scene.behavior

import behavior.BehaviorNode
import behavior.BehaviorStatus
import behavior.leaf.ActionNode
import behavior.leaf.ConditionNode
import drawing.wrapPosition
import radar.generators.GENERATORS
import radar.generators.MovementGeneratorFactory
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.SceneConfig
import kotlin.random.Random

abstract class CatBehaviorManager(private val cat: CatParticle) {
    private val random = MovementGeneratorFactory(generators = GENERATORS).createRandomGenerator()

    val shouldFight = ConditionNode { cat ->
        cat.nearbyCats.any { otherCat ->
            val distance = SceneConfig.metricFunction(cat.coordinates, otherCat.coordinates)
            distance < SceneConfig.fightDist
        }
    }

    val shouldHiss = ConditionNode { cat ->
        cat.nearbyCats.any { otherCat ->
            val distance = SceneConfig.metricFunction(cat.coordinates, otherCat.coordinates)
            if (distance >= SceneConfig.fightDist && distance < SceneConfig.hissDist) {
                val probability = 1 / (distance * distance)
                Random.nextDouble() < probability
            } else {
                false
            }
        }
    }

    val setStateToFight = ActionNode { cat ->
        cat.setCatState(CatStates.FIGHT)
        val deathProbability = 0.2;
        if (Random.nextDouble() < deathProbability) {
            cat.setCatState(CatStates.DEAD)
            BehaviorStatus.FAILURE
        }
        BehaviorStatus.SUCCESS
    }

    val setStateToHiss = ActionNode { cat ->
        cat.setCatState(CatStates.HISS)
        BehaviorStatus.SUCCESS
    }

    val setStateToCalm = ActionNode { cat ->
        cat.setCatState(CatStates.CALM)
        BehaviorStatus.SUCCESS
    }

    val moveRandomList = ActionNode { cat ->
        val offset = random.generate(cat)
        offset.move(cat.coordinates)

        // todo: move out of here
        cat.coordinates = wrapPosition(cat.coordinates)
        BehaviorStatus.SUCCESS
    }

    val sleepAction = ActionNode { cat ->
        if (cat.sleepTicksRemaining <= 0) {
            cat.sleepTicksRemaining = Random.nextInt(20, 50)
            BehaviorStatus.RUNNING
        } else if (cat.sleepTicksRemaining > 0) {
            cat.sleepTicksRemaining--
            if (cat.sleepTicksRemaining == 0) {
                cat.setCatState(CatStates.CALM)
                BehaviorStatus.SUCCESS
            } else {
                BehaviorStatus.RUNNING
            }
        } else {
            BehaviorStatus.SUCCESS
        }
    }


    // Behavior tree for the cat
    protected abstract val behaviorTree: BehaviorNode

    /**
     * Executes the behavior tree for the cat.
     */
    fun tick() {
        behaviorTree.tick(cat)
    }

    /**
     * Builds the behavior tree for the cat.
     */
    protected abstract fun createBehaviorTree(): BehaviorNode
}
