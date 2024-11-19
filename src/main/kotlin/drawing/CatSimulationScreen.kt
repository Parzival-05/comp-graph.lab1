package drawing

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import classes.UIStates
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import drawing.menu.drawDraggableMenu
import kotlinx.coroutines.delay
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.SceneConfig


@Composable
fun updateScene(
    catScene: CatScene, state: MutableState<UIStates>, onSceneUpdated: (Array<CatParticle>) -> Unit
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
    cats: Array<CatParticle>, state: MutableState<UIStates>, config: SceneConfig
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
                    val catRadius = config.catRadius
                    val catOffset = Offset(
                        (cat.coordinates.x).dp.toPx(),
                        (cat.coordinates.y).dp.toPx()
                    )
                    drawCircle(
                        color = currentColor, center = catOffset, radius = catRadius.toFloat()
                    )
                }
            }
            state.value = UIStates.MODELING
            drawDraggableMenu(config = config)
        }
    }
}

@Composable
fun drawModelingTime(timeModeling: Long) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Modeling time: $timeModeling",
                style = MaterialTheme.typography.body1
            )
        }
    }
}
