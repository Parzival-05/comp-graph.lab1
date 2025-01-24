package drawing

import CatSimulation.Companion.BAR_CORNER_RADIUS
import CatSimulation.Companion.BAR_HEIGHT
import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import CatSimulation.Companion.HP_BAR_OFFSET_Y
import CatSimulation.Companion.STROKE_WIDTH
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import behavior.CatRole
import radar.scene.CatParticle
import radar.scene.CatStates
import radar.scene.SceneConfig
import kotlin.time.measureTime

/**
 * Draws the scene by rendering cat particles.
 *
 * This function visualizes cats on a grid by drawing them as circles (for "GHOST" role) or cross marks (for "DEAD" state).
 * It also draws a health bar (HP) for each cat, which changes color based on the HP level.
 *
 * @param cats A list of [CatParticleForDraw] objects representing the cats to be drawn.
 * @param config The [SceneConfig] object containing visual parameters for the scene.
 * @param timeDrawing A mutable state [MutableState<Long>] holding the time spent on drawing.
 */
@Composable
fun drawScene(
    cats: ArrayList<CatParticleForDraw>,
    config: SceneConfig,
    timeDrawing: MutableState<Long>,
) {
    Box(modifier = Modifier.fillMaxSize().drawBehind { drawRect(Color(0xFFae99b8)) }) {
        Box(
            modifier =
                Modifier
                    .size(GRID_SIZE_X.dp, GRID_SIZE_Y.dp)
                    .align(Alignment.Center)
                    .drawBehind { drawRect(Color(0xFFae99b8)) },
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                timeDrawing.value =
                    measureTime {
                        cats.forEach { catParticleForDraw ->
                            val cat = catParticleForDraw.cat
                            val currentColor = getColor(cat)
                            val catRadius = config.catRadius
                            val catOffset =
                                Offset(
                                    catParticleForDraw.from.x.dp
                                        .toPx(),
                                    catParticleForDraw.from.y.dp
                                        .toPx(),
                                )
                            when {
                                cat.role == CatRole.GHOST -> {
                                    drawCircle(
                                        color = currentColor,
                                        center = catOffset,
                                        radius = catRadius.toFloat(),
                                    )
                                }

                                cat.state == CatStates.DEAD -> {
                                    val topLeft = Offset(catOffset.x - catRadius, catOffset.y - catRadius)
                                    val topRight = Offset(catOffset.x + catRadius, catOffset.y - catRadius)
                                    val bottomLeft = Offset(catOffset.x - catRadius, catOffset.y + catRadius)
                                    val bottomRight = Offset(catOffset.x + catRadius, catOffset.y + catRadius)

                                    drawLine(
                                        color = currentColor,
                                        start = topLeft,
                                        end = bottomRight,
                                        strokeWidth = STROKE_WIDTH,
                                    )
                                    drawLine(
                                        color = currentColor,
                                        start = topRight,
                                        end = bottomLeft,
                                        strokeWidth = STROKE_WIDTH,
                                    )
                                }

                                else -> {
                                    drawCircle(
                                        color = currentColor,
                                        center = catOffset,
                                        radius = catRadius.toFloat(),
                                    )

                                    // HP-бар
                                    val barWidth = catRadius * 2.0f
                                    val barHeight = BAR_HEIGHT.dp.toPx()
                                    val barOffset =
                                        Offset(
                                            x = catOffset.x - barWidth / 2,
                                            y = catOffset.y - catRadius - HP_BAR_OFFSET_Y.dp.toPx(),
                                        )

                                    drawRoundRect(
                                        color = Color.Gray,
                                        topLeft = barOffset,
                                        size = Size(barWidth, barHeight),
                                        cornerRadius = CornerRadius(BAR_CORNER_RADIUS.dp.toPx(), BAR_CORNER_RADIUS.dp.toPx()),
                                    )

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
                                        cornerRadius = CornerRadius(BAR_CORNER_RADIUS.dp.toPx(), BAR_CORNER_RADIUS.dp.toPx()),
                                    )
                                }
                            }
                        }
                    }.inWholeMilliseconds
            }
        }
    }
}

/**
 * Displays statistics for the simulation, including modeling time, update time, drawing time, and the number of cats.
 *
 * This function renders the time spent on modeling, updating, and drawing, along with displaying the number of cats
 * that are alive and the number of ghosts.
 *
 * @param timeModeling Time spent on modeling (in milliseconds).
 * @param timeUpdating Time spent on updating (in milliseconds).
 * @param timeDrawing Time spent on drawing (in milliseconds).
 * @param step The simulation step time (in milliseconds).
 * @param cats A list of [CatParticle] objects representing the cats, used for counting the statistics.
 */
@Composable
fun drawStatistics(
    timeModeling: Long,
    timeUpdating: Long,
    timeDrawing: Long,
    step: Long,
    cats: ArrayList<CatParticle>,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomStart,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = "Modeling time: $timeModeling",
                style = MaterialTheme.typography.body1,
            )
            Text(
                text = "Updating time: $timeUpdating",
                style = MaterialTheme.typography.body1,
            )
            Text(
                text = "Drawing time: $timeDrawing",
                style = MaterialTheme.typography.body1,
            )
            Text(
                text = "Step time: $step",
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
