package behavior.tree.decorator

import BaseTest
import behavior.tree.BehaviorStatus
import behavior.tree.leaf.ActionNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import radar.scene.CatParticle
import radar.scene.Point2D

class RepeaterNodeTest : BaseTest() {
    @Test
    fun `verify that an action node is repeated the specified number of times`() {
        val actionNode =
            mock<ActionNode> {
                on { tick(any()) } doReturn BehaviorStatus.SUCCESS
            }
        val repeatCount = 5
        val repeaterNode = behavior.tree.repeat(repeatCount) { actionNode }
        val catParticle = CatParticle(Point2D(0.0, 0.0))
        repeat(repeatCount) {
            repeaterNode.tick(catParticle)
        }
        val status = repeaterNode.tick(catParticle)
        assertEquals(BehaviorStatus.SUCCESS, status)
        verify(actionNode, times(repeatCount)).tick(any())
    }

    @Test
    fun `ensure that an action node stops repeating upon failure`() {
        val actionNode =
            mock<ActionNode> {
                on { tick(any()) } doReturn BehaviorStatus.FAILURE
            }
        val repeatCount = 5
        val repeaterNode = behavior.tree.repeat(repeatCount) { actionNode }
        val catParticle = CatParticle(Point2D(0.0, 0.0))
        val status = repeaterNode.tick(catParticle)
        assertEquals(BehaviorStatus.FAILURE, status)
        verify(actionNode, times(1)).tick(any())
    }

    @Test
    fun `validate that an action node continues to repeat while succeeding`() {
        val actionNode =
            mock<ActionNode> {
                on { tick(any()) } doReturn BehaviorStatus.SUCCESS
            }
        val repeatCount = 3
        val repeaterNode = behavior.tree.repeat(repeatCount) { actionNode }
        val catParticle = CatParticle(Point2D(0.0, 0.0))
        val status = repeaterNode.tick(catParticle)
        assertEquals(BehaviorStatus.RUNNING, status)
        verify(actionNode, times(1)).tick(any())
    }

    @Test
    fun `check behavior when repeat count is set to zero`() {
        val actionNode =
            mock<ActionNode> {
                on { tick(any()) } doReturn BehaviorStatus.SUCCESS
            }
        val repeatCount = 0
        val repeaterNode = behavior.tree.repeat(repeatCount) { actionNode }
        val catParticle = CatParticle(Point2D(0.0, 0.0))
        val status = repeaterNode.tick(catParticle)
        assertEquals(BehaviorStatus.SUCCESS, status)
        verify(actionNode, times(0)).tick(any())
    }

    @Test
    fun `confirm that an action node can be repeated indefinitely`() {
        val actionNode =
            mock<ActionNode> {
                on { tick(any()) } doReturn BehaviorStatus.SUCCESS
            }
        val repeatCount = Int.MAX_VALUE
        val repeaterNode = behavior.tree.repeat(repeatCount) { actionNode }
        val catParticle = CatParticle(Point2D(0.0, 0.0))
        var status: BehaviorStatus
        var executions = 0
        do {
            status = repeaterNode.tick(catParticle)
            executions++
        } while (status == BehaviorStatus.SUCCESS && executions < 10)
        assertEquals(BehaviorStatus.RUNNING, status)
        verify(actionNode, times(executions)).tick(any())
    }
}
