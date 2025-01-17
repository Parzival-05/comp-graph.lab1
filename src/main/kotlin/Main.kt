import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import classes.UIStates
import drawing.drawScene
import drawing.drawStatistics
import drawing.updateScene
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import radar.generators.CatGenerator
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.SceneConfig
import kotlin.time.measureTime

fun main() =
    application {
        SceneConfig.loadConfig("config.properties")
        val catGenerator = CatGenerator()
        val cats = ArrayList<CatParticle>()
        for (i in 1..SceneConfig.particleCount) {
            cats.add(catGenerator.generate())
        }
        val catScene = CatScene(cats, SceneConfig)
        val state = mutableStateOf(UIStates.MODELING)

        Window(onCloseRequest = ::exitApplication, title = "Cat Lab UI") {
            var currentCats: Array<CatParticle> by remember { mutableStateOf(emptyArray()) }
            var timeModeling by remember { mutableStateOf(0L) }
            val cs = rememberCoroutineScope { Dispatchers.Default }

            LaunchedEffect(SceneConfig.particleCount) {
                while (cats.size < SceneConfig.particleCount) {
                    cats.add(catGenerator.generate())
                }
                while (cats.size > SceneConfig.particleCount) {
                    cats.removeLast()
                }
                currentCats = cats.toTypedArray()
            }

            LaunchedEffect(Unit) {
                cs.launch {
                    while (true) {
                        if (!SceneConfig.isOnPause) {
                            if (state.value == UIStates.MODELING) {
                                timeModeling = measureTime(catScene::updateScene).inWholeMilliseconds
                                state.value = UIStates.UPDATE_DATA
                            }
                            if (timeModeling < SceneConfig.tau) {
                                val sleepTimeMSBatch = 5L
                                var totalSleepTime = timeModeling - 3
                                while (totalSleepTime < SceneConfig.tau) {
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
            drawStatistics(timeModeling, catScene.particles)
        }
    }
