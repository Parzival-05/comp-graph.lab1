package drawing

import GRID_SIZE_X
import GRID_SIZE_Y
import FRAMES_PER_TAU
import CAT_RADIUS
import TAU
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import radar.generators.MoveGenerator
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.CatStates
import kotlin.system.measureTimeMillis


data class CatMutable(
    var cat: MutableState<CatParticle>,
)

@Composable
fun catSimulationScreen(scene: CatScene, moveGenerator: MoveGenerator) {
    val mutableCats = remember {
        SnapshotStateList<CatMutable>().apply {
            addAll(scene.particles.map { CatMutable(mutableStateOf(it)) })
        }
    }

    LaunchedEffect(null) {
        while (true) {
            // TODO: efficient recomposition
            val time = measureTimeMillis { scene.updateScene(moveGenerator) }
            println(time)
            delay(TAU - time)
            val mutableCatsValues = mutableCats.map { it.cat.value }.toHashSet()
            val newCats = mutableSetOf<CatParticle>()
            scene.particles.forEach {
                if (it !in mutableCatsValues) {
                    newCats.add(it)
                }
            }
            val toRemove = mutableListOf<CatMutable>()
            mutableCats.forEachIndexed { index, mutableCat -> // force recomposition
                val cat = mutableCat.cat.value
                if (cat in scene.particles) {
                    mutableCats[index] = mutableCats[index].copy(cat = mutableStateOf(cat))
                } else {
                    toRemove.add(mutableCat)
                }
            }
//          toRemove.forEach { mutableCats.remove(it) } TODO: removing leads to teleportation of cats
            newCats.forEach { mutableCats.add(CatMutable(mutableStateOf(it))) }
        }
    }

    Box(modifier = Modifier.fillMaxSize().drawBehind { drawRect(Color(0xFFae99b8)) }) {
        Box(modifier = Modifier.size(GRID_SIZE_X.dp, GRID_SIZE_Y.dp).align(Alignment.Center)
            .drawBehind { drawRect(Color(0xFFb5f096)) }) {
            mutableCats.forEach { cat ->
                drawCat(cat)
            }
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

        val targetColor = getColorForState(cat.cat.value.state)
        for (i in 1..FRAMES_PER_TAU) {
            animatedX += (cat.cat.value.coordinates.x - animatedX) / FRAMES_PER_TAU
            animatedY += (cat.cat.value.coordinates.y - animatedY) / FRAMES_PER_TAU
            currentColor = interpolateColor(currentColor, targetColor, 5f / FRAMES_PER_TAU)
            delay(TAU / FRAMES_PER_TAU)
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val catOffset = androidx.compose.ui.geometry.Offset(
            animatedX.dp.toPx(), animatedY.dp.toPx()
        )
        drawCircle(color = currentColor, center = catOffset, radius = CAT_RADIUS.dp.toPx())
    }
}

fun getColorForState(state: CatStates): Color {
    return when (state) {
        CatStates.CALM -> Color.White
        CatStates.HISS -> Color.Gray
        CatStates.FIGHT -> Color.Black
    }
}

fun interpolateColor(start: Color, end: Color, fraction: Float): Color {
    val r = start.red + (end.red - start.red) * fraction
    val g = start.green + (end.green - start.green) * fraction
    val b = start.blue + (end.blue - start.blue) * fraction
    val a = start.alpha + (end.alpha - start.alpha) * fraction
    return Color(r, g, b, a)
}
