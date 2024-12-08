package drawing

import androidx.compose.ui.graphics.Color
import radar.scene.CatStates

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
    }
