package drawing

import androidx.compose.ui.graphics.Color
import radar.scene.CatStates

fun getColorForState(state: CatStates): Color {
    return when (state) {
        CatStates.CALM -> Color.White
        CatStates.HISS -> Color.Gray
        CatStates.FIGHT -> Color.Black
    }
}