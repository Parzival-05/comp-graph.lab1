package drawing

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import CatSimulation.Companion.PARTICLE_COUNT
import radar.logging.GlobalInteractionLog
import classes.UIStates
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.CatStates
import kotlin.math.sqrt
import drawing.logging.draggableLogWithButton
import kotlinx.coroutines.delay


@Composable
fun updateScene(
    catScene: CatScene,
    state: MutableState<UIStates>,
    onSceneUpdated: (Array<CatParticle>) -> Unit
) {
    LaunchedEffect(Unit) {
        while (true) {
            if (state.value == UIStates.UPDATE_DATA) {
                onSceneUpdated(catScene.particles.map { it }.toTypedArray()) // Передача нового списка
                state.value = UIStates.DRAWING
            }
            delay(3)
        }
    }
}

@Composable
fun drawScene(
    cats: Array<CatParticle>, // Передача неизменяемого списка
    state: MutableState<UIStates>
) {
    Box(modifier = Modifier.fillMaxSize().drawBehind { drawRect(Color(0xFFae99b8)) }) {
        Box(modifier = Modifier.size(GRID_SIZE_X.dp, GRID_SIZE_Y.dp).align(Alignment.Center)
            .drawBehind { drawRect(Color(0xFFb5f096)) }) {
            LaunchedEffect(Unit) {
                while (state.value != UIStates.DRAWING) {
                    delay(3)
                }
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                cats.forEach { cat ->
                    val currentColor = getColorForState(cat.state)
                    val catRadius = (50.0 / sqrt(PARTICLE_COUNT.toFloat())).coerceAtLeast(1.0)
                    val catOffset = Offset(
                        (cat.coordinates.x - catRadius / 2).dp.toPx(),
                        (cat.coordinates.y - catRadius / 2).dp.toPx()
                    )
                    drawCircle(
                        color = currentColor, center = catOffset, radius = catRadius.toFloat()
                    )
                }
            }
            state.value = UIStates.MODELING
            draggableLogWithButton(GlobalInteractionLog)
        }
    }
}
