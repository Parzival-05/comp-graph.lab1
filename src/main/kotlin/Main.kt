import CatSimulation.Companion.PARTICLE_COUNT
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import classes.UIStates
import drawing.drawModelingTime
import drawing.drawScene
import drawing.updateScene
import kotlinx.coroutines.*
import radar.generators.CatGenerator
import radar.generators.MoveGenerator
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.SceneConfig
import kotlin.time.measureTime


fun main() = application {
    val sceneConfig = SceneConfig()
    val catGenerator = CatGenerator()
    val cats = ArrayList<CatParticle>()
    for (i in 1..PARTICLE_COUNT) {
        cats.add(catGenerator.generate())
    }
    val catScene = CatScene(cats, sceneConfig)
    val moveGenerator = MoveGenerator(sceneConfig)
    val state = mutableStateOf(UIStates.MODELING)

    Window(onCloseRequest = ::exitApplication, title = "Cat Lab UI") {
        var currentCats: Array<CatParticle> by remember { mutableStateOf(emptyArray()) }
        var timeModeling by remember { mutableStateOf(0L) }
        val cs = rememberCoroutineScope { Dispatchers.Default }
        LaunchedEffect(Unit) {
            cs.launch {
                while (true) {
                    if (!sceneConfig.isOnPause) {
                        if (state.value == UIStates.MODELING) {
                            timeModeling = measureTime { catScene.updateScene(moveGenerator) }.inWholeMilliseconds
                            state.value = UIStates.UPDATE_DATA
                        }
                        if (timeModeling < sceneConfig.tau) {
                            val sleepTimeMSBatch = 5L
                            var totalSleepTime = timeModeling - 3
                            while (totalSleepTime < sceneConfig.tau) {
                                delay(sleepTimeMSBatch)
                                totalSleepTime += sleepTimeMSBatch
                            }
                        }
                    }
                    delay(3)
                }
            }
        }
        updateScene(catScene, state) { updatedCats ->
            currentCats = updatedCats
        }
        drawScene(currentCats, state, catScene.sceneConfig)
        drawModelingTime(timeModeling)
    }
}