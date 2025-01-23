package behavior.tree.leaf

import BaseTest
import behavior.tree.BehaviorStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.Point2D

class ConditionNodeTest : BaseTest() {
    private fun createConditionNode(condition: (CatParticle) -> Boolean): ConditionNode = ConditionNode(condition)

    @Test
    fun `validate condition returns success for a calm cat`() {
        val conditionNode = createConditionNode { it.state == CatStates.CALM }
        val cat = CatParticle(Point2D(0.0, 0.0), CatStates.CALM)
        val result = conditionNode.tick(cat)
        assertEquals(BehaviorStatus.SUCCESS, result)
    }

    @Test
    fun `validate condition returns failure for a hissing cat`() {
        val conditionNode = createConditionNode { it.state == CatStates.CALM }
        val cat = CatParticle(Point2D(0.0, 0.0), CatStates.HISS)
        val result = conditionNode.tick(cat)
        assertEquals(BehaviorStatus.FAILURE, result)
    }
}
