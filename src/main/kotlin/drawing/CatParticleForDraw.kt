package drawing

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import radar.scene.CatParticle
import radar.scene.Point2D

data class CatParticleForDraw(
    val cat: CatParticle,
    var from: Point2D,
    var to: Point2D,
) {
    fun nextStep(progress: Double) {
        from.x += (to.x - from.x) * progress
        from.y += (to.y - from.y) * progress
    }

    fun updateGoal() {
        val newCoords = cat.coordinates.copy()

        if (kotlin.math.abs(newCoords.x - to.x) > GRID_SIZE_X / 2) {
            from.x = newCoords.x // Мгновенный переход
        }

        if (kotlin.math.abs(newCoords.y - to.y) > GRID_SIZE_Y / 2) {
            from.y = newCoords.y // Мгновенный переход
        }

        to = newCoords
    }
}
