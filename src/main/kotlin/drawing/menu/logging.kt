package drawing.menu

import CatSimulation.Companion.MAX_INTERACTIONS_DISPLAYED
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
import radar.scene.CatInteraction

@Composable
fun drawInteractionLog(interactionLog: List<CatInteraction>) {
    val interactionsToDisplay = interactionLog.takeLast(MAX_INTERACTIONS_DISPLAYED).asReversed()
    LazyColumn(
        modifier = Modifier.height(200.dp).fillMaxWidth(),
    ) {
        item {
            Text(
                "Recent interactions:",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp,
            )
        }
        items(interactionsToDisplay) { interaction ->
            Text(
                text =
                    buildAnnotatedString {
                        append("[${interaction.time}] ")
                        val color = getColorForState(interaction.type)
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = color)) {
                            append("${interaction.type} ")
                        }
                        append("between Cat ${interaction.particleId1} and Cat ${interaction.particleId2}")
                    },
                fontSize = 12.sp,
                color = Color.White,
            )
        }
    }
}
