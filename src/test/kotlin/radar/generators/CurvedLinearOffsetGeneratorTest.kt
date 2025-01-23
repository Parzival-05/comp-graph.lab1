package radar.generators

import BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import radar.scene.CatParticle
import radar.scene.SceneConfig
import kotlin.math.cos
import kotlin.math.sin

class CurvedLinearOffsetGeneratorTest : BaseTest() {
    private val linearVelocityX = 1.0
    private val linearVelocityY = 1.0
    private val curveFactor = 0.05
    private val frequency = 1.0
    private val generator =
        CurvedLinearOffsetGenerator<CatParticle>(linearVelocityX, linearVelocityY, curveFactor, frequency)

    private fun computeExpectedOffset(
        time: Double,
        linearVelocityX: Double,
        linearVelocityY: Double,
        curveFactor: Double,
        frequency: Double,
        maxSpeed: Double,
    ): Pair<Double, Double> {
        val x = (linearVelocityX + curveFactor * sin(2 * Math.PI * frequency * time)) * maxSpeed
        val y = (linearVelocityY + curveFactor * cos(2 * Math.PI * frequency * time)) * maxSpeed
        return Pair(x, y)
    }

    @Test
    fun `verify that the curved linear offset generator produces offsets based on linear velocity and curve factor`() {
        val (expectedX, expectedY) =
            computeExpectedOffset(
                time = 0.0,
                linearVelocityX = linearVelocityX,
                linearVelocityY = linearVelocityY,
                curveFactor = curveFactor,
                frequency = frequency,
                maxSpeed = SceneConfig.maxParticleSpeed,
            )

        val offset = generator.generate(particle)

        assertEquals(expectedX, offset.x)
        assertEquals(expectedY, offset.y)
    }

    @Test
    fun `ensure that the time progression affects the generated offsets`() {
        val firstOffset = generator.generate(particle)
        val secondOffset = generator.generate(particle)
        assertNotEquals(firstOffset.x, secondOffset.x)
        assertNotEquals(firstOffset.y, secondOffset.y)
    }

    @Test
    fun `validate that the generated offsets are consistent with the frequency parameter`() {
        val updatedFrequency = 2.0
        val generatorWithFrequency =
            CurvedLinearOffsetGenerator<CatParticle>(linearVelocityX, linearVelocityY, curveFactor, updatedFrequency)

        val (expectedX, expectedY) =
            computeExpectedOffset(
                time = 0.0,
                linearVelocityX = linearVelocityX,
                linearVelocityY = linearVelocityY,
                curveFactor = curveFactor,
                frequency = updatedFrequency,
                maxSpeed = SceneConfig.maxParticleSpeed,
            )

        val offset = generatorWithFrequency.generate(particle)

        assertEquals(expectedX, offset.x)
        assertEquals(expectedY, offset.y)
    }

    @Test
    fun `check that the generated offsets scale with the maximum particle speed`() {
        val maxSpeed = 10.0
        SceneConfig.maxParticleSpeed = maxSpeed

        val (expectedX, expectedY) =
            computeExpectedOffset(
                time = 0.0,
                linearVelocityX = linearVelocityX,
                linearVelocityY = linearVelocityY,
                curveFactor = curveFactor,
                frequency = frequency,
                maxSpeed = maxSpeed,
            )

        val offset = generator.generate(particle)

        assertEquals(expectedX, offset.x)
        assertEquals(expectedY, offset.y)
    }

    @Test
    fun `confirm that the offset generator can handle varying curve factors`() {
        val varyingCurveFactors = listOf(0.01, 0.1, 0.2)

        varyingCurveFactors.forEach { curve ->
            val generatorWithCurveFactor =
                CurvedLinearOffsetGenerator<CatParticle>(linearVelocityX, linearVelocityY, curve)

            val offset = generatorWithCurveFactor.generate(particle)
            assertNotEquals(0.0, offset.x)
            assertNotEquals(0.0, offset.y)
        }
    }

    @Test
    fun `ensure that the offset generator produces valid offsets for different linear velocities`() {
        val velocities = listOf(0.0, 1.0, 2.0)

        velocities.forEach { velocity ->
            val generatorWithVelocity =
                CurvedLinearOffsetGenerator<CatParticle>(velocity, velocity, curveFactor)

            val offset = generatorWithVelocity.generate(particle)
            assertTrue(offset.x != 0.0 || offset.y != 0.0)
        }
    }
}
