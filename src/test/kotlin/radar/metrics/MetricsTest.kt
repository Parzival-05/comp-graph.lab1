package radar.metrics

import net.jqwik.api.Arbitraries
import net.jqwik.api.Arbitrary
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.api.Provide
import radar.scene.Point2D
import kotlin.math.abs
import kotlin.reflect.KFunction2

class MetricsTest {
    @Provide
    fun points(): Arbitrary<Point2D> =
        Arbitraries.doubles().between(-100.0, 100.0).flatMap { x ->
            Arbitraries.doubles().between(-100.0, 100.0).map { y ->
                Point2D(x, y)
            }
        }

    @Provide
    fun distanceMetrics(): Arbitrary<KFunction2<Point2D, Point2D, Double>> = Arbitraries.of(::euclidean, ::manhattan, ::greatCircle)

    @Property
    fun `distance is non-negative`(
        @ForAll("points") point1: Point2D,
        @ForAll("points") point2: Point2D,
        @ForAll("distanceMetrics") metric: (Point2D, Point2D) -> Double,
    ) {
        val distance = metric(point1, point2)
        assert(distance >= 0) { "Distance should never be negative. Got $distance using ${metric::class.simpleName}." }
    }

    @Property
    fun `distance is zero if and only if points are identical`(
        @ForAll("points") point: Point2D,
        @ForAll("distanceMetrics") metric: (Point2D, Point2D) -> Double,
    ) {
        val distanceToSelf = metric(point, point)
        assert(almostEqual(distanceToSelf, 0.0)) {
            "Distance to self should be zero. Got $distanceToSelf using " + "${
                metric::class.simpleName
            }."
        }
    }

    @Property
    fun `distance zero implies identical points`(
        @ForAll("points") point1: Point2D,
        @ForAll("points") point2: Point2D,
        @ForAll("distanceMetrics") metric: (Point2D, Point2D) -> Double,
    ) {
        val distance = metric(point1, point2)
        if (almostEqual(distance, 0.0)) {
            assert(almostEqual(point1, point2)) {
                "Distance zero implies points should be identical using " + "${metric::class.simpleName}."
            }
        }
    }

    @Property
    fun `distance is symmetric`(
        @ForAll("points") point1: Point2D,
        @ForAll("points") point2: Point2D,
        @ForAll("distanceMetrics") metric: (Point2D, Point2D) -> Double,
    ) {
        val distance1 = metric(point1, point2)
        val distance2 = metric(point2, point1)
        assert(almostEqual(distance1, distance2)) {
            "Distance is not symmetric. Got $distance1 and $distance2 using " + "${metric::class.simpleName}."
        }
    }

    @Property
    fun `triangle inequality holds`(
        @ForAll("points") point1: Point2D,
        @ForAll("points") point2: Point2D,
        @ForAll("points") point3: Point2D,
        @ForAll("distanceMetrics") metric: (Point2D, Point2D) -> Double,
    ) {
        val d1 = metric(point1, point2)
        val d2 = metric(point2, point3)
        val d3 = metric(point1, point3)
        assert(d1 + d2 > d3 || almostEqual(d1 + d2, d3)) {
            "Triangle inequality violated. Got $d1 + $d2 < $d3 using " + "${
                metric::class.simpleName
            }."
        }
    }

    private fun relativeError(
        a: Double,
        b: Double,
        epsilonMin: Double = 1e-6,
    ): Double {
        val denominator = maxOf(abs(a), abs(b), epsilonMin)
        return abs(a - b) / denominator
    }

    private fun almostEqual(
        a: Double,
        b: Double,
        tolerance: Double = 1e-3,
    ): Boolean = relativeError(a, b) <= tolerance

    private fun almostEqual(
        a: Point2D,
        b: Point2D,
    ): Boolean = almostEqual(a.x, b.x) && almostEqual(a.y, b.y)
}
