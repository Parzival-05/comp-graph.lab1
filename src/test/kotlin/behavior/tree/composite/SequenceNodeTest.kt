package behavior.tree.composite

import BaseTest
import behavior.tree.BehaviorStatus
import behavior.tree.leaf.ActionNode
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import radar.scene.CatParticle
import radar.scene.CatStates
import kotlin.test.assertEquals

class SequenceNodeTest : BaseTest() {
    private fun createSequenceNode(vararg actions: (CatParticle) -> BehaviorStatus): SequenceNode {
        val sequenceNode = SequenceNode(actions.map { ActionNode(it) }.toMutableList())
        return sequenceNode
    }

    @Test
    fun `sequence of actions executes successfully when all conditions are met`() {
        val sequenceNode =
            createSequenceNode(
                { _: CatParticle -> BehaviorStatus.SUCCESS },
                { _: CatParticle -> BehaviorStatus.SUCCESS },
            )
        val cat = CatParticle(mock(), CatStates.CALM)
        val result = sequenceNode.tick(cat)
        assertEquals(BehaviorStatus.SUCCESS, result)
        assertEquals(CatStates.CALM, cat.state)
    }

    @Test
    fun `sequence execution stops when an action fails`() {
        val sequenceNode =
            createSequenceNode(
                { _: CatParticle -> BehaviorStatus.SUCCESS },
                { _: CatParticle -> BehaviorStatus.FAILURE },
            )
        val cat = CatParticle(mock(), CatStates.CALM)
        val result = sequenceNode.tick(cat)
        assertEquals(BehaviorStatus.FAILURE, result)
    }

    @Test
    fun `sequence handles multiple actions with varying outcomes`() {
        val sequenceNode =
            createSequenceNode(
                { _: CatParticle -> BehaviorStatus.SUCCESS },
                { _: CatParticle -> BehaviorStatus.FAILURE },
                { _: CatParticle -> BehaviorStatus.SUCCESS },
            )
        val cat = CatParticle(mock(), CatStates.CALM)
        val result = sequenceNode.tick(cat)
        assertEquals(BehaviorStatus.FAILURE, result)
    }

    @Test
    fun `sequence executes all actions when conditions are met`() {
        val sequenceNode =
            createSequenceNode(
                { _: CatParticle -> BehaviorStatus.SUCCESS },
                { _: CatParticle -> BehaviorStatus.SUCCESS },
            )
        val cat = CatParticle(mock(), CatStates.CALM)
        val result = sequenceNode.tick(cat)
        assertEquals(BehaviorStatus.SUCCESS, result)
    }

    @Test
    fun `sequence can be repeated multiple times`() {
        val repeatCount = 3
        val sequenceNode =
            createSequenceNode(
                { _: CatParticle -> BehaviorStatus.SUCCESS },
            )
        val cat = CatParticle(mock(), CatStates.CALM)
        repeat(repeatCount) {
            sequenceNode.tick(cat)
        }
        assertEquals(BehaviorStatus.SUCCESS, sequenceNode.tick(cat))
    }
}
