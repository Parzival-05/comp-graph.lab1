package radar.scene.behavior

import behavior.BehaviorNode
import behavior.BehaviorStatus
import behavior.leaf.ActionNode
import drawing.wrapPosition
import radar.generators.GENERATORS
import radar.generators.MovementGeneratorFactory
import radar.logging.log
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.SceneConfig
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
            distance < SceneConfig.fightDist && otherCat.state != CatStates.SLEEPING
        }
        if (closestCat == null) return@ActionNode BehaviorStatus.FAILURE
        cat.setCatState(CatStates.FIGHT)
        val deathProbability = 0.2
        log(cat, closestCat, CatStates.FIGHT)
        if (Random.nextDouble() < deathProbability) {
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
