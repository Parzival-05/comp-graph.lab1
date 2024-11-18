package drawing.logging

import CatSimulation.Companion.MAX_INTERACTIONS_DISPLAYED
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import drawing.getColorForState
import radar.scene.CatInteraction
import kotlin.math.roundToInt

@Composable
fun draggableLogWithButton(interactionLog: MutableList<CatInteraction>) {
    var isLogVisible by remember { mutableStateOf(true) }

    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    Box(modifier = Modifier.offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                offsetX.value += dragAmount.x
                offsetY.value += dragAmount.y
            }
        }) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Button(onClick = { isLogVisible = !isLogVisible }) {
                Text(if (isLogVisible) "Hide Log" else "Show Log")
            }

            if (isLogVisible) {
                Box(
                    modifier = Modifier.width(250.dp).wrapContentHeight().background(Color(0x88000000)).padding(8.dp)
                ) {
                    drawInteractionLog(interactionLog)
                }
            }
        }
    }
}

@Composable
fun drawInteractionLog(interactionLog: List<CatInteraction>) {
    val interactionsToDisplay = interactionLog.takeLast(MAX_INTERACTIONS_DISPLAYED).asReversed()
    LazyColumn(
        modifier = Modifier.height(200.dp).fillMaxWidth()
    ) {
        item {
            Text(
                "Recent interactions:", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp
            )
        }
        items(interactionsToDisplay) { interaction ->
            Text(
                text = buildAnnotatedString {
                    append("[${interaction.time}] ")
                    val color = getColorForState(interaction.type)
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = color)) {
                        append("${interaction.type} ")
                    }
                    append("between Cat ${interaction.particleId1} and Cat ${interaction.particleId2}")
                }, fontSize = 12.sp, color = Color.White
            )
        }
    }
}