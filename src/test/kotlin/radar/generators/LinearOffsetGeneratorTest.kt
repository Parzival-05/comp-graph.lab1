package radar.generators

import BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import radar.scene.CatParticle
import radar.scene.Offset2D
import radar.scene.SceneConfig

class LinearOffsetGeneratorTest : BaseTest() {
    private fun computeExpectedOffset(
        velocityX: Double,
        velocityY: Double,
    ): Offset2D = Offset2D(velocityX * SceneConfig.maxParticleSpeed, velocityY * SceneConfig.maxParticleSpeed)

    @Test
    fun `verify that linear movement offsets are generated correctly`() {
        val generator = LinearOffsetGenerator<CatParticle>(2.0, 3.0)
        val offset = generator.generate(particle)
        val expectedOffset = computeExpectedOffset(2.0, 3.0)

        assertEquals(expectedOffset.x, offset.x)
        assertEquals(expectedOffset.y, offset.y)
    }

    @Test
    fun `ensure offsets are consistent for the same particle and velocities`() {
        val generator = LinearOffsetGenerator<CatParticle>(2.0, 3.0)
        val offset1 = generator.generate(particle)
        val offset2 = generator.generate(particle)

        assertEquals(offset1.x, offset2.x)
        assertEquals(offset1.y, offset2.y)
    }

    @Test
    fun `validate that offsets are generated with zero velocities`() {
        val generator = LinearOffsetGenerator<CatParticle>(0.0, 0.0)
        val offset = generator.generate(particle)
        val expectedOffset = computeExpectedOffset(0.0, 0.0)

        assertEquals(expectedOffset.x, offset.x)
        assertEquals(expectedOffset.y, offset.y)
    }

    @Test
    fun `check behavior with negative velocities`() {
        val generator = LinearOffsetGenerator<CatParticle>(-2.0, -3.0)
        val offset = generator.generate(particle)
        val expectedOffset = computeExpectedOffset(-2.0, -3.0)

        assertEquals(expectedOffset.x, offset.x)
        assertEquals(expectedOffset.y, offset.y)
    }

    @Test
    fun `confirm offsets are generated correctly with varying velocities`() {
        val generator = LinearOffsetGenerator<CatParticle>(1.5, 4.5)
        val offset = generator.generate(particle)
        val expectedOffset = computeExpectedOffset(1.5, 4.5)

        assertEquals(expectedOffset.x, offset.x)
        assertEquals(expectedOffset.y, offset.y)
    }

    @Test
    fun `ensure offsets are generated correctly when velocities are modified`() {
        var generator = LinearOffsetGenerator<CatParticle>(2.0, 3.0)
        var offset = generator.generate(particle)
        var expectedOffset = computeExpectedOffset(2.0, 3.0)

        assertEquals(expectedOffset.x, offset.x)
        assertEquals(expectedOffset.y, offset.y)

        generator = LinearOffsetGenerator(5.0, 6.0)
        offset = generator.generate(particle)
        expectedOffset = computeExpectedOffset(5.0, 6.0)

        assertEquals(expectedOffset.x, offset.x)
        assertEquals(expectedOffset.y, offset.y)
    }

    @Test
    fun `validate that offsets are generated consistently across multiple particles`() {
        val generator = LinearOffsetGenerator<CatParticle>(2.0, 3.0)
        val particle1: CatParticle = mock()
        val particle2: CatParticle = mock()

        val offset1 = generator.generate(particle1)
        val offset2 = generator.generate(particle2)
        val expectedOffset = computeExpectedOffset(2.0, 3.0)

        assertEquals(expectedOffset.x, offset1.x)
        assertEquals(expectedOffset.y, offset1.y)
        assertEquals(expectedOffset.x, offset2.x)
        assertEquals(expectedOffset.y, offset2.y)
    }
}
