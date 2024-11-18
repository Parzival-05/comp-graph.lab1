package radar.scene

class SceneConfig {
    companion object {
        enum class MetricType { EUCLIDEAN, MANHATTAN, CURVED }
    }

    var maxParticleSpeed = 1
    var metric: MetricType = MetricType.EUCLIDEAN
    var fightDist = 1 // r_0
    var hissDist = 2 // R_0
        set(value) { // invariant: R_0 > r_0
            field = if (value < fightDist) {
                throw RuntimeException("Hiss distance can't be less than fight distance.")
            } else {
                value
            }
        }
}
