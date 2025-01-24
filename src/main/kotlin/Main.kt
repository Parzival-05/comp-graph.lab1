import CatSimulation.Companion.FPS
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import classes.ModelingStates
import classes.TaskThread
import classes.UIStates
import drawing.CatParticleForDraw
import drawing.drawScene
import drawing.drawStatistics
import drawing.menu.drawDraggableMenu
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import radar.generators.CatGenerator
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.SceneConfig
import kotlin.math.floor
import kotlin.time.measureTime

fun main() =
    application {
        SceneConfig.loadConfig("config.properties")
        val catGenerator = CatGenerator()
        val cats by remember { mutableStateOf(ArrayList<CatParticle>()) }
        val catsToDraw: ArrayList<CatParticleForDraw> by remember { mutableStateOf(ArrayList()) }

        fun addCat(catParticle: CatParticle) {
            cats.add(catParticle)
            val coords = catParticle.coordinates.copy()
            catsToDraw.add(CatParticleForDraw(catParticle, coords, coords))
        }

        fun removeCat(cat: CatParticle) {
            cats.remove(cat)
            catsToDraw.removeIf { it.cat == cat }
        }

        for (i in 1..SceneConfig.particleCount) {
            addCat(catGenerator.generate())
        }
        val catScene = CatScene(cats, SceneConfig)
        catScene.updateScene()
        catsToDraw.forEach { it.updateGoal() }

        val state = mutableStateOf(UIStates.READY_TO_DRAW)
        var modelingState = ModelingStates.FINISHED

        val frameDurationMs = floor(1000.toDouble() / FPS).toInt()

        fun calculateStepsCount() = SceneConfig.tau / frameDurationMs
        var steps = calculateStepsCount()
        val step = mutableStateOf(0L)
        val scope = rememberCoroutineScope()
        val coroutineTimeoutTime = 1L
        Window(onCloseRequest = ::exitApplication, title = "Cat Lab UI") {
            var timeModeling by remember { mutableStateOf(0L) }
            var totalTimeModeling = 0L
            var timeUpdating by remember { mutableStateOf(0L) }
            val timeDrawing = mutableStateOf(0L)
            val needToUpdateConfig = mutableStateOf(false)

            LaunchedEffect(SceneConfig.particleCount, state.value) {
                while (cats.size < SceneConfig.particleCount) {
                    addCat(catGenerator.generate())
                }
                while (cats.size > SceneConfig.particleCount) {
                    removeCat(cats.last())
                }
            }

            scope.launch {
                val taskThread = TaskThread()
                while (true) {
                    if (!SceneConfig.isOnPause) {
                        if (state.value == UIStates.DRAWING_IS_FINISHED) {
                            catsToDraw.forEach {
                                it.updateGoal()
                            }
                            totalTimeModeling = 0
                            modelingState = ModelingStates.MODELING
                            state.value = UIStates.READY_TO_DRAW
                            taskThread
                                .submitTask {
                                    totalTimeModeling =
                                        measureTime {
                                            catScene.updateScene()
                                        }.inWholeMilliseconds
                                    modelingState = ModelingStates.FINISHED
                                }
                            while (modelingState != ModelingStates.FINISHED) {
                                delay(coroutineTimeoutTime)
                            }
                            timeModeling = totalTimeModeling
                            delay(SceneConfig.tau - 1 - timeModeling)
                        }
                    }
                    delay(coroutineTimeoutTime)
                }
            }
            var totalProgress = 0.0
            var progress: Double
            scope.launch {
                while (true) {
                    if (!SceneConfig.isOnPause) {
                        if (state.value == UIStates.READY_TO_DRAW) {
                            if (step.value < steps) {
                                timeUpdating =
                                    measureTime {
                                        steps = calculateStepsCount()
                                        progress = (1 - totalProgress) / (steps - step.value)
                                        totalProgress += progress
                                        catsToDraw.forEach {
                                            it.nextStep(progress)
                                        }
                                        step.value += 1
                                    }.inWholeMilliseconds
                                delay(frameDurationMs - 1 - timeUpdating - timeDrawing.value)
                            } else {
                                state.value = UIStates.DRAWING_IS_FINISHED
                                step.value = 0
                                totalProgress = 0.0
                            }
                        }
                    }
                    delay(coroutineTimeoutTime)
                }
            }
            drawScene(catsToDraw, catScene.sceneConfig, timeDrawing)
            drawDraggableMenu(catScene.sceneConfig)
            drawStatistics(timeModeling, timeUpdating, timeDrawing.value, step.value, catScene.particles)
        }
    }
