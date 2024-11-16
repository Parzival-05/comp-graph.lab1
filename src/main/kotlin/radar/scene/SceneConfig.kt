package radar.scene

class SceneConfig {
    companion object {
        enum class MetricType { EUCLIDEAN, MANHATTAN, CURVED }
    }

    var maxParticleSpeed = 10
    var metric: MetricType = MetricType.EUCLIDEAN
    var fightDist = 10 // r_0
    var hissDist = 50 // R_0
        set(value) { // invariant: R_0 > r_0
            field = if (value < fightDist) {
                throw RuntimeException("Hiss distance can't be less than fight distance.")
            } else {
                value
            }
        }

}
