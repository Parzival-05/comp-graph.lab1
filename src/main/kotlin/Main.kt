import CatSimulation.Companion.PARTICLE_COUNT
import CatSimulation.Companion.TAU
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import classes.UIStates
import drawing.CatMutable
import drawing.drawScene
import drawing.updateScene
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.delay
import radar.generators.CatGenerator
import radar.generators.MoveGenerator
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.SceneConfig
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime


fun main() = application {
    val keepAliveTime = 100L
    val workQueue = SynchronousQueue<Runnable>()
    val workerPool: ExecutorService = ThreadPoolExecutor(
        2, 2, keepAliveTime, TimeUnit.SECONDS, workQueue
    )
    val sceneConfig = SceneConfig()
    val catGenerator = CatGenerator()
    val cats = ArrayList<CatParticle>()
    for (i in 1..PARTICLE_COUNT) {
        cats.add(catGenerator.generate())
    }
    val catScene = CatScene(cats, sceneConfig)
    val moveGenerator = MoveGenerator(sceneConfig)
    val state = atomic(UIStates.MODELING)
    val timeModeling = atomic(0L) // TODO: remove it from the release ver.
    workerPool.execute {
        while (true) {
            if (state.value == UIStates.MODELING) {
                timeModeling.value = measureTime { catScene.updateScene(moveGenerator) }.inWholeMilliseconds
                println("modeling = $timeModeling ms.")
                state.value = UIStates.UPDATE_DATA
            }
        }
    }
    Window(onCloseRequest = ::exitApplication, title = "Cat Lab UI") {
        Dialog(
            onDismissRequest = { /* Do something when back button pressed */ },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            val mutableCats = remember {
                SnapshotStateList<CatMutable>().apply {
                    addAll(catScene.particles.map { CatMutable(mutableStateOf(it)) })
                }
            }
            val timeUpdateData = atomic(0L) // TODO: remove it from the release ver.
            updateScene(
                catScene, mutableCats, state, timeUpdateData
            ) {
                delay(TAU - (timeModeling.value + timeUpdateData.value))
            }
            drawScene(mutableCats, state, catScene.sceneConfig)
        }
    }
    workerPool.shutdown()
}
