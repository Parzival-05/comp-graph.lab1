package behavior.tree.composite

import BaseTest
import behavior.tree.BehaviorStatus
import behavior.tree.leaf.ActionNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import radar.scene.CatParticle
import radar.scene.CatStates

class SelectorNodeTest : BaseTest() {
    private fun createSelectorNode(vararg actions: (CatParticle) -> BehaviorStatus): SelectorNode {
        val selectorNode = SelectorNode(actions.map { ActionNode(it) }.toMutableList())
        return selectorNode
    }

    @Test
    fun `selector node executes child nodes until one succeeds`() {
        val selectorNode =
            createSelectorNode(
                { _: CatParticle -> BehaviorStatus.FAILURE },
                { _: CatParticle -> BehaviorStatus.SUCCESS },
            )
        val cat = CatParticle(mock(), CatStates.CALM)
        val result = selectorNode.tick(cat)
        assertEquals(BehaviorStatus.SUCCESS, result)
    }

    @Test
    fun `selector node handles all child nodes failing`() {
        val selectorNode =
            createSelectorNode(
                { _: CatParticle -> BehaviorStatus.FAILURE },
                { _: CatParticle -> BehaviorStatus.FAILURE },
            )
        val cat = CatParticle(mock(), CatStates.CALM)
        val result = selectorNode.tick(cat)
        assertEquals(BehaviorStatus.FAILURE, result)
    }

    @Test
    fun `selector node executes child nodes in order until one succeeds`() {
        val selectorNode =
            createSelectorNode(
                { _: CatParticle -> BehaviorStatus.FAILURE },
                { _: CatParticle -> BehaviorStatus.FAILURE },
                { _: CatParticle -> BehaviorStatus.SUCCESS },
            )
        val cat = CatParticle(mock(), CatStates.CALM)
        val result = selectorNode.tick(cat)
        assertEquals(BehaviorStatus.SUCCESS, result)
    }

    @Test
    fun `selector node returns RUNNING status when child nodes are still processing`() {
        val selectorNode =
            createSelectorNode(
                { _: CatParticle -> BehaviorStatus.RUNNING },
                { _: CatParticle -> BehaviorStatus.SUCCESS },
            )
        val cat = CatParticle(mock(), CatStates.CALM)
        val result = selectorNode.tick(cat)
        assertEquals(BehaviorStatus.RUNNING, result)
    }

    @Test
    fun `selector node with no child nodes returns FAILURE`() {
        val selectorNode = SelectorNode()
        val cat = CatParticle(mock(), CatStates.CALM)
        val result = selectorNode.tick(cat)
        assertEquals(BehaviorStatus.FAILURE, result)
    }
}
