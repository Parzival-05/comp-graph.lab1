import org.junit.jupiter.api.BeforeEach
import radar.scene.Point2D
import radar.scene.SceneConfig
import kotlin.math.abs
import kotlin.test.assertTrue

abstract class BaseTest {
    @BeforeEach
    fun setUp() {
        SceneConfig.loadConfig("test_config.properties")
    }

    protected fun almostEqual(
        a: Double,
        b: Double,
        tolerance: Double = 1e-3,
    ): Boolean = relativeError(a, b) <= tolerance

    protected fun almostEqual(
        a: Point2D,
        b: Point2D,
    ): Boolean = almostEqual(a.x, b.x) && almostEqual(a.y, b.y)

    protected fun assertAlmostEqual(
        a: Double,
        b: Double,
        message: String = "Assertion failed: $a != $b",
    ) = assertTrue(almostEqual(a, b), message)

    protected fun assertAlmostEqual(
        a: Point2D,
        b: Point2D,
        message: String = "Assertion failed: $a != $b",
    ) = assertTrue(almostEqual(a, b), message)

    private fun relativeError(
        a: Double,
        b: Double,
        epsilonMin: Double = 1e-6,
    ): Double {
        val denominator = maxOf(abs(a), abs(b), epsilonMin)
        return abs(a - b) / denominator
    }
}
