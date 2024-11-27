package drawing

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import classes.UIStates
import drawing.menu.drawDraggableMenu
import kotlinx.coroutines.delay
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.SceneConfig

/**
 * Updates the scene by checking the current UI state and passing updated particle data to the UI.
 *
 * @param catScene The current scene containing all cat particles.
 * @param state A mutable state representing the current UI state.
 * @param onSceneUpdated A callback function to receive the updated array of CatParticles.
 */
@Composable
fun updateScene(
    catScene: CatScene,
    state: MutableState<UIStates>,
    onSceneUpdated: (Array<CatParticle>) -> Unit,
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

/**
 * Draws the scene by rendering cat particles, adjusting UI state accordingly.
 *
 * @param cats An array of CatParticles to be drawn.
 * @param state A mutable state representing the current UI state.
 * @param config The scene configuration containing visual parameters.
 */
@Composable
fun drawScene(
    cats: Array<CatParticle>,
    state: MutableState<UIStates>,
    config: SceneConfig,
) {
    Box(modifier = Modifier.fillMaxSize().drawBehind { drawRect(Color(0xFFae99b8)) }) {
        Box(
            modifier =
                Modifier
                    .size(GRID_SIZE_X.dp, GRID_SIZE_Y.dp)
                    .align(Alignment.Center)
                    .drawBehind { drawRect(Color(0xFFae99b8)) },
        ) {
            LaunchedEffect(Unit) {
                while (state.value != UIStates.DRAWING) {
                    delay(3)
                }
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                cats.forEach { cat ->
                    val currentColor = getColorForState(cat.state)
                    val catRadius = config.catRadius
                    val catOffset =
                        Offset(
                            (cat.coordinates.x).dp.toPx(),
                            (cat.coordinates.y).dp.toPx(),
                        )
                    drawCircle(
                        color = currentColor,
                        center = catOffset,
                        radius = catRadius.toFloat(),
                    )
                }
            }
            state.value = UIStates.MODELING
            drawDraggableMenu(config = config)
        }
    }
}

/**
 * Displays the modeling time in the bottom-left corner of the screen.
 *
 * @param timeModeling The time taken for modeling, in milliseconds.
 */
@Composable
fun drawModelingTime(timeModeling: Long) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomStart,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            println(timeModeling)
            Text(
                text = "Modeling time: $timeModeling",
                style = MaterialTheme.typography.body1,
            )
        }
    }
}
