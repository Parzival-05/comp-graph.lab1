package radar.scene

import CatSimulation.Companion.MAX_PARTICLE_COUNT
import CatSimulation.Companion.MIN_PARTICLE_COUNT
import radar.metrics.euclidean
import radar.metrics.greatCircle
import radar.metrics.manhattan

/**
 * Configuration class containing parameters for the simulation scene.
 */
class SceneConfig {
    companion object {
        /**
         * Enum representing the types of metrics used for distance calculations.
         */
        enum class MetricType { EUCLIDEAN, MANHATTAN, GREAT_CIRCLE }
    }

    /**
     * Maximum speed of particles.
     */
    var maxParticleSpeed = 1.0

    /**
     * The metric used for distance calculations.
     */
    var metric: MetricType = MetricType.EUCLIDEAN

    /**
     * The function corresponding to the selected metric.
     */
    val metricFunction: ((Point2D, Point2D) -> Double)
        get() {
            return when (metric) {
                SceneConfig.Companion.MetricType.EUCLIDEAN -> ::euclidean
                SceneConfig.Companion.MetricType.MANHATTAN -> ::manhattan
                SceneConfig.Companion.MetricType.GREAT_CIRCLE -> ::greatCircle
            }
        }

    /**
     * The distance within which cats will fight.
     */
    var fightDist = 1 // r_0

    /**
     * The distance within which cats may hiss.
     *
     * Must be greater than fightDist.
     */
    var hissDist = 2 // R_0
        set(value) { // invariant: R_0 > r_0
            field = if (value < fightDist) {
                throw RuntimeException("Hiss distance can't be less than fight distance.")
            } else {
                value
            }
        }

    /**
     * The radius used to draw cats.
     */
    var catRadius = 1

    /**
     * The time interval (in milliseconds) between scene updates.
     */
    var tau = 500L


    /**
     * Indicates if the simulation is paused.
     */
    var isOnPause = false

    /**
     * The desired number of particles in the scene.
     */
    var particleCount: Int = MIN_PARTICLE_COUNT
        set(value) {
            field = value.coerceIn(MIN_PARTICLE_COUNT, MAX_PARTICLE_COUNT)
        }
}
