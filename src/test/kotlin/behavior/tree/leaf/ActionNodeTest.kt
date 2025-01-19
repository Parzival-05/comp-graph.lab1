package behavior.tree.leaf

import BaseTest
import behavior.tree.BehaviorStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import radar.scene.CatParticle
import radar.scene.CatStates

class ActionNodeTest : BaseTest() {
    @Test
    fun `action node executes a defined action successfully`() {
        val cat = CatParticle(mock(), CatStates.CALM)
        val actionNode =
            ActionNode { particle ->
                particle.state = CatStates.HISS
                BehaviorStatus.SUCCESS
            }
        val result = actionNode.tick(cat)
        assertEquals(BehaviorStatus.SUCCESS, result)
        assertEquals(CatStates.HISS, cat.state)
    }

    @Test
    fun `action node handles a failure in the action execution`() {
        val cat = CatParticle(mock(), CatStates.CALM)
        val actionNode =
            ActionNode {
                BehaviorStatus.FAILURE
            }
        val result = actionNode.tick(cat)
        assertEquals(BehaviorStatus.FAILURE, result)
        assertEquals(CatStates.CALM, cat.state)
    }

    @Test
    fun `action node executes multiple actions in sequence`() {
        val cat = CatParticle(mock(), CatStates.CALM)
        val firstAction =
            ActionNode { particle ->
                particle.state = CatStates.HISS
                BehaviorStatus.SUCCESS
            }
        val secondAction =
            ActionNode { particle ->
                particle.state = CatStates.FIGHT
                BehaviorStatus.SUCCESS
            }

        firstAction.tick(cat)
        assertEquals(CatStates.HISS, cat.state)

        secondAction.tick(cat)
        assertEquals(CatStates.FIGHT, cat.state)
    }

    @Test
    fun `action node maintains state across executions`() {
        val cat = CatParticle(mock(), CatStates.CALM)
        val actionNode =
            ActionNode { particle ->
                particle.state = CatStates.SLEEPING
                BehaviorStatus.SUCCESS
            }
        actionNode.tick(cat)
        assertEquals(CatStates.SLEEPING, cat.state)

        actionNode.tick(cat)
        assertEquals(CatStates.SLEEPING, cat.state)
    }

    @Test
    fun `action node can be reused with different cat particles`() {
        val firstCat = CatParticle(mock(), CatStates.CALM)
        val secondCat = CatParticle(mock(), CatStates.CALM)
        val actionNode =
            ActionNode { particle ->
                particle.state = CatStates.DEAD
                BehaviorStatus.SUCCESS
            }

        actionNode.tick(firstCat)
        assertEquals(CatStates.DEAD, firstCat.state)

        actionNode.tick(secondCat)
        assertEquals(CatStates.DEAD, secondCat.state)
    }
}
