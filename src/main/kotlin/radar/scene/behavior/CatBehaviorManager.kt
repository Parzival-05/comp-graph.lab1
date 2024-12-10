package radar.scene.behavior

import behavior.BehaviorNode
import behavior.BehaviorStatus
import behavior.leaf.ActionNode
import behavior.leaf.ConditionNode
import drawing.wrapPosition
import radar.generators.GENERATORS
import radar.generators.MovementGeneratorFactory
import radar.logging.log
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.SceneConfig
import radar.scene.behavior.gang.CatRole
import kotlin.random.Random

abstract class CatBehaviorManager(private val cat: CatParticle) {
    private val random = MovementGeneratorFactory(generators = GENERATORS).createRandomGenerator()

    val shouldHiss = ActionNode { cat ->
        val closestCat = cat.nearbyCats.find { otherCat ->
            val distance = SceneConfig.metricFunction(cat.coordinates, otherCat.coordinates)
            if (distance >= SceneConfig.fightDist && distance < SceneConfig.hissDist) {
                val probability = 1 / (distance * distance)
                Random.nextDouble() < probability
            } else {
                false
            }
        }
        if (closestCat == null) return@ActionNode BehaviorStatus.FAILURE
        log(cat, closestCat, CatStates.HISS)
        cat.setCatState(CatStates.HISS)
        BehaviorStatus.SUCCESS
    }

    val shouldFight = ActionNode { cat ->
        val closestCat = cat.nearbyCats.find { otherCat ->
            val distance = SceneConfig.metricFunction(cat.coordinates, otherCat.coordinates)
            // todo: this is too much
            distance < SceneConfig.fightDist && otherCat.state != CatStates.SLEEPING
                && otherCat.state != CatStates.DEAD
        }
        if (closestCat == null) return@ActionNode BehaviorStatus.FAILURE
        cat.setCatState(CatStates.FIGHT)
        cat.hp -= Random.nextInt(1, 10)
        log(cat, closestCat, CatStates.FIGHT)
        if (cat.hp < 0) {
            cat.setCatState(CatStates.DEAD)
            BehaviorStatus.FAILURE
        } else {
            BehaviorStatus.SUCCESS
        }
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

    val shouldBecomeGhost = ConditionNode { _ ->
        val ghostProbability = 10e-2
        Random.nextDouble() < ghostProbability
    }

    val setRoleToGhost = ActionNode { cat ->
        cat.setCatRole(CatRole.GHOST)
        BehaviorStatus.SUCCESS
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
