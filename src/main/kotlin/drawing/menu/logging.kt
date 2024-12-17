package drawing.menu

import CatSimulation.Companion.MAX_LOGS_DISPLAYED
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import drawing.getColorForState
import radar.logging.Loggable
import radar.scene.CatInteraction
import radar.scene.CatStateChange

@Composable
fun drawInteractionLog(logs: List<Loggable>) {
    val logsToDisplay = logs.takeLast(MAX_LOGS_DISPLAYED).asReversed()
    LazyColumn(
        modifier = Modifier.height(200.dp).fillMaxWidth(),
    ) {
        item {
            Text(
                "Recent actions:",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp,
            )
        }
        items(logsToDisplay) { log ->
            when (log) {
                is CatInteraction ->
                    Text(
                        text =
                        buildAnnotatedString {
                            append("[${log.time}] ")
                            val color = getColorForState(log.type)
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = color)) {
                                append("${log.type} ")
                            }
                            append("between Cat ${log.particleId1} and Cat ${log.particleId2}")
                        },
                        fontSize = 12.sp,
                        color = Color.White,
                    )

                is CatStateChange ->
                    Text(
                        text =
                        buildAnnotatedString {
                            append("[${log.time}] ")
                            val color = getColorForState(log.type)
                            append("Cat ${log.catId} changed state to ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = color)) {
                                append("${log.type}")
                            }
                        },
                        fontSize = 12.sp,
                        color = Color.White,
                    )
            }
        }
    }
}
