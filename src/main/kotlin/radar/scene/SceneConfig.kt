package radar.scene

import radar.metrics.euclidean
import radar.metrics.greatCircle
import radar.metrics.manhattan

class SceneConfig {
    companion object {
        enum class MetricType { EUCLIDEAN, MANHATTAN, GREAT_CIRCLE }
    }

    var maxParticleSpeed = 1
    var metric: MetricType = MetricType.EUCLIDEAN
    val metricFunction: ((Point2D, Point2D) -> Float)
        get() {
            return when (metric) {
                SceneConfig.Companion.MetricType.EUCLIDEAN -> ::euclidean
                SceneConfig.Companion.MetricType.MANHATTAN -> ::manhattan
                SceneConfig.Companion.MetricType.GREAT_CIRCLE -> ::greatCircle
            }
        }
    var fightDist = 1 // r_0
    var hissDist = 2 // R_0
        set(value) { // invariant: R_0 > r_0
            field = if (value < fightDist) {
                throw RuntimeException("Hiss distance can't be less than fight distance.")
            } else {
                value
            }
        }
    var catRadius = 1
    var tau = 500L
    var isOnPause = false
}
