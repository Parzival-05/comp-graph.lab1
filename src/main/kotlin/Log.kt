import CatSimulation.Companion.MAX_INTERACTIONS_DISPLAYED
import androidx.compose.runtime.mutableStateListOf
import core.base.BaseInteraction
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import radar.scene.CatCollision
import radar.scene.CatStates
import java.sql.Timestamp
import kotlin.math.roundToInt

val GlobalInteractionLog = mutableStateListOf<BaseInteraction<CatStates>>()
@Composable
fun DraggableLogWithButton() {
    var isLogVisible by remember { mutableStateOf(true) }

    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX.value += dragAmount.x
                    offsetY.value += dragAmount.y
                }
            }
    ) {
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
                    modifier = Modifier
                        .width(250.dp)
                        .wrapContentHeight()
                        .background(Color(0x88000000))
                        .padding(8.dp)
                ) {
                    InteractionLog(GlobalInteractionLog)
                }
            }
        }
    }
}

@Composable
fun InteractionLog(interactions: List<BaseInteraction<CatStates>>) {
    val interactionsToDisplay = interactions.takeLast(MAX_INTERACTIONS_DISPLAYED).asReversed()

    LazyColumn(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
    ) {
        item {
            Text(
                "Recent interactions:",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )
        }

        items(interactionsToDisplay) { interaction ->
            Text(
                text = buildAnnotatedString {
                    append("[${interaction.time}] ")
                    val color = when (interaction.type) {
                        CatStates.CALM -> Color.White
                        CatStates.HISS -> Color.Gray
                        CatStates.FIGHT -> Color.Black
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = color)) {
                        append("${interaction.type} ")
                    }
                    append("between Cat ${interaction.particleId1} and Cat ${interaction.particleId2}")
                },
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}

fun log(collision: CatCollision, newState: CatStates) {
    val time = Timestamp(System.currentTimeMillis())
    val cat1Id = collision.particle1.id
    val cat2Id = collision.particle2.id
    GlobalInteractionLog.add(BaseInteraction(time, cat1Id, cat2Id, newState))
}