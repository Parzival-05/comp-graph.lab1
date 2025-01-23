package radar.generators

import BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import radar.scene.CatParticle
import radar.scene.Point2D
import radar.scene.SceneConfig
import kotlin.math.sqrt

class SeekTargetOffsetGeneratorTest : BaseTest() {
    private fun calculateExpectedOffset(
        from: Point2D,
        to: Point2D,
    ): Point2D {
        val dx = to.x - from.x
        val dy = to.y - from.y
        val magnitude = sqrt(dx * dx + dy * dy)
        return if (magnitude == 0.0) {
            Point2D(0.0, 0.0)
        } else {
            Point2D(dx / magnitude * SceneConfig.maxParticleSpeed, dy / magnitude * SceneConfig.maxParticleSpeed)
        }
    }

    @Test
    fun `verify offset generation towards a target point`() {
        val targetPoint = Point2D(5.0, 5.0)
        val generator = SeekTargetOffsetGenerator<CatParticle>(targetPoint)

        val offset = generator.generate(particle)
        val expectedOffset = calculateExpectedOffset(particlePosition, targetPoint)

        assertEquals(expectedOffset.x, offset.x)
        assertEquals(expectedOffset.y, offset.y)
    }

    @Test
    fun `ensure offset generation handles particles at the target location`() {
        val targetPoint = Point2D(5.0, 5.0)
        val generator = SeekTargetOffsetGenerator<CatParticle>(targetPoint)

        // Particle is placed at the target's exact location
        particle.coordinates = targetPoint
        val offset = generator.generate(particle)

        assertEquals(0.0, offset.x)
        assertEquals(0.0, offset.y)
    }

    @Test
    fun `validate offset generation with negative coordinates`() {
        val targetPoint = Point2D(-5.0, -5.0)
        val generator = SeekTargetOffsetGenerator<CatParticle>(targetPoint)

        val offset = generator.generate(particle)
        val expectedOffset = calculateExpectedOffset(particlePosition, targetPoint)

        assertEquals(expectedOffset.x, offset.x)
        assertEquals(expectedOffset.y, offset.y)
    }

    @Test
    fun `confirm offset generation with varying target distances`() {
        val distances =
            listOf(
                Point2D(1.0, 1.0),
                Point2D(20.0, 20.0),
                Point2D(50.0, 50.0),
            )

        distances.forEach { target ->
            particle.coordinates = Point2D(0.0, 0.0) // Reset particle position before each case
            val generator = SeekTargetOffsetGenerator<CatParticle>(target)

            val offset = generator.generate(particle)
            val expectedOffset = calculateExpectedOffset(particle.coordinates, target)

            assertEquals(expectedOffset.x, offset.x)
            assertEquals(expectedOffset.y, offset.y)
        }
    }

    @Test
    fun `ensure offset generation is consistent across multiple invocations`() {
        val targetPoint = Point2D(5.0, 5.0)
        val generator = SeekTargetOffsetGenerator<CatParticle>(targetPoint)

        val firstOffset = generator.generate(particle)
        val secondOffset = generator.generate(particle)

        assertEquals(firstOffset.x, secondOffset.x)
        assertEquals(firstOffset.y, secondOffset.y)
    }

    @Test
    fun `validate offset generation with extreme values`() {
        val targetPoint = Point2D(Double.MAX_VALUE, Double.MAX_VALUE)
        val generator = SeekTargetOffsetGenerator<CatParticle>(targetPoint)

        val offset = generator.generate(particle)
        val expectedOffset = calculateExpectedOffset(particlePosition, targetPoint)

        assertEquals(expectedOffset.x, offset.x)
        assertEquals(expectedOffset.y, offset.y)
    }
}
