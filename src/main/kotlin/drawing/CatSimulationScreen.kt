package drawing

import CatSimulation.Companion.CAT_RADIUS
import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import classes.UIStates
import DraggableLogWithButton
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.atomicfu.AtomicLong
import kotlinx.atomicfu.AtomicRef
import kotlinx.coroutines.delay
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.CatStates
import kotlin.system.measureTimeMillis


data class CatMutable(
    var cat: MutableState<CatParticle>
)


@Composable
fun updateScene(
    catScene: CatScene,
    mutableCats: SnapshotStateList<CatMutable>,
    state: AtomicRef<UIStates>,
    timeUpdateData: AtomicLong,
    cont: suspend (() -> Unit)
) {
    LaunchedEffect(Unit) {
        while (true) {
            if (state.value == UIStates.UPDATE_DATA) {
                timeUpdateData.value = measureTimeMillis {
                    catScene.particles.forEachIndexed { index, cat ->
                        mutableCats.add(index, CatMutable(mutableStateOf(cat)))
                    }
                    if (catScene.particles.size < mutableCats.size) {
                        mutableCats.removeRange(catScene.particles.size, mutableCats.size - 1)
                    }
                }
                println("updateData = ${timeUpdateData.value} ms.")
                state.value = UIStates.DRAWING
            }
            delay(3)
            cont()
        }
    }
}

@Composable
fun drawScene(
    mutableCats: MutableList<CatMutable>, state: AtomicRef<UIStates>
) {
    Box(modifier = Modifier.fillMaxSize().drawBehind { drawRect(Color(0xFFae99b8)) }) {
        Box(modifier = Modifier.size(GRID_SIZE_X.dp, GRID_SIZE_Y.dp).align(Alignment.Center)
            .drawBehind { drawRect(Color(0xFFb5f096)) }) {
            LaunchedEffect(Unit) {
                while (state.value != UIStates.DRAWING) {
                    delay(3)
                }
            }
            mutableCats.forEach { drawCat(it) }
            state.value = UIStates.MODELING

            DraggableLogWithButton()
        }
    }
}

@Composable
fun drawCat(cat: CatMutable) {
    var animatedX by remember { mutableStateOf(cat.cat.value.coordinates.x) }
    var animatedY by remember { mutableStateOf(cat.cat.value.coordinates.y) }
    var currentColor by remember { mutableStateOf(getColorForState(cat.cat.value.state)) }

    LaunchedEffect(
        cat
    ) {
        animatedX = cat.cat.value.coordinates.x
        animatedY = cat.cat.value.coordinates.y
        currentColor = getColorForState(cat.cat.value.state)
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val catOffset = androidx.compose.ui.geometry.Offset(
            (animatedX - CAT_RADIUS / 2).dp.toPx(), (animatedY - CAT_RADIUS / 2).dp.toPx()
        )
        val catSize = Size((CAT_RADIUS * 2).toFloat(), (CAT_RADIUS * 2).toFloat())
        drawRect(
            color = currentColor, topLeft = catOffset, size = catSize
        )
    }
}

fun getColorForState(state: CatStates): Color {
    return when (state) {
        CatStates.CALM -> Color.White
        CatStates.HISS -> Color.Gray
        CatStates.FIGHT -> Color.Black
    }
}
