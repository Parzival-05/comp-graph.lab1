package drawing.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import radar.logging.InteractionLogger
import radar.scene.SceneConfig
import kotlin.math.roundToInt

@Composable
fun drawDraggableMenu(config: SceneConfig) {
    var isLogVisible by remember { mutableStateOf(true) }
    var showSettings by remember { mutableStateOf(false) }

    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }

    Box(
        modifier =
            Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX.value += dragAmount.x
                        offsetY.value += dragAmount.y
                    }
                }.padding(8.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = { isLogVisible = !isLogVisible }) {
                    Text(if (isLogVisible) "Hide Log" else "Show Log")
                }

                Button(onClick = { showSettings = true }) {
                    Text("Settings")
                }

                Button(onClick = {
                    config.isOnPause = !config.isOnPause
                }) {
                    val text =
                        if (!config.isOnPause) {
                            "Pause"
                        } else {
                            "Resume"
                        }
                    Text(text)
                }
            }

            if (isLogVisible) {
                Box(
                    modifier =
                        Modifier
                            .width(250.dp)
                            .wrapContentHeight()
                            .background(Color(0x88000000))
                            .padding(8.dp),
                ) {
                    drawInteractionLog(InteractionLogger.getLogs())
                }
            }

            if (showSettings) {
                sceneSettingsMenu(config = config, onClose = { showSettings = false })
            }
        }
    }
}
