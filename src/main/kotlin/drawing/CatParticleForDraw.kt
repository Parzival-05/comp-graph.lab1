package drawing

import radar.scene.CatParticle
import radar.scene.Point2D
import radar.scene.SceneConfig

data class CatParticleForDraw(
    val cat: CatParticle,
    var from: Point2D,
    var to: Point2D,
) {
    fun nextStep(progress: Double) {
        val dX = to.x - from.x
        val dY = to.y - from.y


        val squaredDistance = dX * dX + dY * dY
        val maxSpeedSquared = SceneConfig.maxParticleSpeed * SceneConfig.maxParticleSpeed

        if (squaredDistance >= 100 * maxSpeedSquared) {
            from.x = to.x
            from.y = to.y
            println("$squaredDistance, ${100 * maxSpeedSquared}")
        } else {
            from.x += (to.x - from.x) * progress
            from.y += (to.y - from.y) * progress
        }
    }

    fun updateGoal() {
        to = cat.coordinates.copy()
    }
}
