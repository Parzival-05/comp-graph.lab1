package drawing

import CatSimulation
import androidx.compose.ui.graphics.Color
import behavior.CatRole
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.Point2D
import kotlin.math.abs

/**
 * Retrieves the color associated with a given cat state.
 *
 * @param state The state of the cat (CALM, HISS, FIGHT).
 * @return The color corresponding to the given state.
 */
fun getColorForState(state: CatStates): Color =
    when (state) {
        CatStates.CALM -> Color.White
        CatStates.HISS -> Color.Gray
        CatStates.FIGHT -> Color.Black
        CatStates.SLEEPING -> Color.Blue
        CatStates.DEAD -> Color.Red
    }

fun getColor(cat: CatParticle): Color =
    when (cat.role) {
        CatRole.DEFAULT -> getColorForState(cat.state)
        CatRole.POSSESSED -> Color.Green
        CatRole.GHOST -> Color(0x80ff2120)
    }

fun wrapPosition(position: Point2D): Point2D {
    val wrappedX =
        when {
            position.x < 0.0 -> CatSimulation.GRID_SIZE_X - abs(position.x)
            position.x > CatSimulation.GRID_SIZE_X -> position.x - CatSimulation.GRID_SIZE_X
            else -> position.x
        }

    val wrappedY =
        when {
            position.y < 0.0 -> CatSimulation.GRID_SIZE_Y - abs(position.y)
            position.y > CatSimulation.GRID_SIZE_Y -> position.y - CatSimulation.GRID_SIZE_Y
            else -> position.y
        }

    return Point2D(wrappedX, wrappedY)
}
