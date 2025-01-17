package radar.scene.behavior

import behavior.BehaviorNode
import behavior.BehaviorStatus
import behavior.action
import behavior.condition
import drawing.wrapPosition
import radar.generators.MovementGeneratorFactory
import radar.generators.RANDOM_GENERATORS
import radar.logging.InteractionLogger.logInteraction
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.SceneConfig
import radar.scene.behavior.gang.CatRole
import kotlin.random.Random

/**
 * Cat behavior manager implemented using behavior trees. Each action is presented as a node,
 * e.g. moving and fighting. [CatBehaviorManager] is also responsible for cat state.
 *
 * @param cat The cat particle
 */
abstract class CatBehaviorManager(
    private val cat: CatParticle,
) {
    private val randomMovement = MovementGeneratorFactory(generators = RANDOM_GENERATORS).createRandomGenerator()

    val shouldHiss =
        action { cat ->
            val closestCat =
                cat.nearbyCats.find { otherCat ->
                    val distance = SceneConfig.metricFunction(cat.coordinates, otherCat.coordinates)
                    if (distance >= SceneConfig.fightDist && distance < SceneConfig.hissDist) {
                        val probability = 1 / (distance * distance)
                        Random.nextDouble() < probability
                    } else {
                        false
                    }
                }
            if (closestCat == null) return@action BehaviorStatus.FAILURE
            logInteraction(cat, closestCat, CatStates.HISS)
            cat.setCatState(CatStates.HISS)
            BehaviorStatus.SUCCESS
        }

    val shouldFight =
        action { cat ->
            val closestCat =
                cat.nearbyCats.find { otherCat ->
                    val distance = SceneConfig.metricFunction(cat.coordinates, otherCat.coordinates)
                    // todo: this is too much
                    distance < SceneConfig.fightDist &&
                        otherCat.state != CatStates.SLEEPING &&
                        otherCat.state != CatStates.DEAD
                }
            if (closestCat == null) return@action BehaviorStatus.FAILURE
            cat.setCatState(CatStates.FIGHT)
            cat.hp -= Random.nextInt(1, 10)
            logInteraction(cat, closestCat, CatStates.FIGHT)
            if (cat.hp < 0) {
                cat.setCatState(CatStates.DEAD)
                BehaviorStatus.FAILURE
            } else {
                BehaviorStatus.SUCCESS
            }
        }

    val setStateToCalm =
        action { cat ->
            cat.setCatState(CatStates.CALM)
            BehaviorStatus.SUCCESS
        }

    val moveRandomList =
        action { cat ->
            cat.previousCoordinates = cat.coordinates.copy()
            val offset = randomMovement.generate(cat)
            offset.move(cat.coordinates)

            cat.coordinates = wrapPosition(cat.coordinates)
            BehaviorStatus.SUCCESS
        }

    val shouldBecomeGhost =
        condition { _ ->
            val ghostProbability = 10e-2
            Random.nextDouble() < ghostProbability
        }

    val setRoleToGhost =
        action { cat ->
            cat.setCatRole(CatRole.GHOST)
            BehaviorStatus.SUCCESS
        }

    // Behavior tree for the cat
    private val behaviorTree: BehaviorNode by lazy { createBehaviorTree() }

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
