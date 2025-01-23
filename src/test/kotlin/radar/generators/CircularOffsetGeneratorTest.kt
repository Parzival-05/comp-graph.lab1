package radar.generators

import BaseTest
import core.base.BaseParticle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import radar.scene.Offset2D
import radar.scene.Point2D
import radar.scene.SceneConfig
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CircularOffsetGeneratorTest : BaseTest() {
    private val radius = 10.0
    private val angle = 0.0
    private val angularVelocity = SceneConfig.maxParticleSpeed / 100.0

    private fun computeExpectedOffset(
        radius: Double,
        angle: Double,
    ): Offset2D {
        val expectedX = radius * cos(angle) * SceneConfig.maxParticleSpeed
        val expectedY = radius * sin(angle) * SceneConfig.maxParticleSpeed
        return Offset2D(expectedX, expectedY)
    }

    private fun normalizeAngle(angle: Double): Double = angle % (2 * PI)

    @Test
    fun `verify circular movement offset generation with a specified radius`() {
        val generator = CircularOffsetGenerator<BaseParticle<Point2D, Offset2D>>(radius, angle)
        val offset = generator.generate(particle)
        val expectedOffset = computeExpectedOffset(radius, angle)

        assertEquals(expectedOffset.x, offset.x)
        assertEquals(expectedOffset.y, offset.y)
        assertTrue(normalizeAngle(angle) in 0.0..(2 * PI))
    }

    @Test
    fun `ensure angle wraps correctly after full rotation`() {
        val generator = CircularOffsetGenerator<BaseParticle<Point2D, Offset2D>>(radius, angle)
        repeat(100) {
            generator.generate(particle)
        }
        assertTrue(normalizeAngle(angle) in 0.0..(2 * PI))
    }

    @Test
    fun `validate offset generation with varying angular velocities`() {
        val generator = CircularOffsetGenerator<BaseParticle<Point2D, Offset2D>>(radius, angle)
        generator.generate(particle)

        val firstOffset = generator.generate(particle)
        val newAngle = normalizeAngle(angle + angularVelocity)
        val expectedOffset = computeExpectedOffset(radius, newAngle)

        assertEquals(expectedOffset.x, firstOffset.x)
        assertEquals(expectedOffset.y, firstOffset.y)
        assertTrue(newAngle > angle)
    }

    @Test
    fun `confirm offset scaling based on maximum particle speed`() {
        val generator = CircularOffsetGenerator<BaseParticle<Point2D, Offset2D>>(radius, angle)
        val offset = generator.generate(particle)
        val expectedOffset = computeExpectedOffset(radius, angle)

        assertEquals(expectedOffset.x, offset.x)
        assertEquals(expectedOffset.y, offset.y)
    }

    @Test
    fun `validate behavior when radius is set to zero`() {
        val generator = CircularOffsetGenerator<BaseParticle<Point2D, Offset2D>>(0.0, angle)
        val offset = generator.generate(particle)
        assertEquals(0.0, offset.x)
        assertEquals(0.0, offset.y)
        assertEquals(0.0, angle)
    }
}
