package drawing

import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import classes.UIStates
import drawing.menu.drawDraggableMenu
import kotlinx.coroutines.delay
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.CatStates
import radar.scene.SceneConfig
import radar.scene.behavior.gang.CatRole

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
                    val catOffset = Offset(
                        cat.coordinates.x.dp.toPx(),
                        cat.coordinates.y.dp.toPx()
                    )

                    when {
                        cat.role == CatRole.GHOST -> {
                            // Призраки рисуются как прозрачные кружки
                            drawCircle(
                                color = Color(0x80ff2120), // Полупрозрачный красный
                                center = catOffset,
                                radius = catRadius.toFloat()
                            )
                        }
                        cat.state == CatStates.DEAD -> {
                            // Мертвые коты отображаются как кресты
                            val lineLength = catRadius * 2.0f
                            val topLeft = Offset(catOffset.x - lineLength / 2, catOffset.y - lineLength / 2)
                            val topRight = Offset(catOffset.x + lineLength / 2, catOffset.y - lineLength / 2)
                            val bottomLeft = Offset(catOffset.x - lineLength / 2, catOffset.y + lineLength / 2)
                            val bottomRight = Offset(catOffset.x + lineLength / 2, catOffset.y + lineLength / 2)

                            drawLine(
                                color = currentColor,
                                start = topLeft,
                                end = bottomRight,
                                strokeWidth = 4f,
                            )
                            drawLine(
                                color = currentColor,
                                start = topRight,
                                end = bottomLeft,
                                strokeWidth = 4f
                            )
                        } else -> {
                            drawCircle(
                                // todo: so lazy rn
                                color = if (cat.role != CatRole.POSSESSED) currentColor else Color.Green,
                                center = catOffset,
                                radius = catRadius.toFloat(),
                            )

                            // HP-бар
                            val barWidth = catRadius * 2.0f
                            val barHeight = 8.dp.toPx()
                            val barOffset =
                                Offset(
                                x = catOffset.x - barWidth / 2,
                                y = catOffset.y - catRadius - 16.dp.toPx(),
                            )

                        drawRoundRect(
                            color = Color.Gray,
                            topLeft = barOffset,
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                        )

                        // todo: scene config !!!
                        val hpPercentage = cat.hp / 100f
                        val filledWidth = barWidth * hpPercentage
                        val color =
                            when {
                                hpPercentage > 0.67 -> Color.Green
                                hpPercentage > 0.33 -> Color.Yellow
                                else -> Color.Red
                            }
                            drawRoundRect(
                                color = color,
                                topLeft = barOffset,
                                size = Size(filledWidth, barHeight),
                                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                            )
                        }
                    }
                }
            }
            state.value = UIStates.MODELING
            drawDraggableMenu(config = config)
        }
    }
}

// todo: docs
@Composable
fun drawStatistics(
    timeModeling: Long,
    cats: ArrayList<CatParticle>,
) {
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
        Column(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Alive: ${cats.count { it.state != CatStates.DEAD }}",
                    style = MaterialTheme.typography.body1,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Ghost: ${cats.count { it.role == CatRole.GHOST }}",
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}
