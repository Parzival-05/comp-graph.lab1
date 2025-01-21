import CatSimulation.Companion.FPS
import CatSimulation.Companion.GRID_SIZE_X
import CatSimulation.Companion.GRID_SIZE_Y
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import classes.ModelingStates
import classes.UIStates
import drawing.drawScene
import drawing.drawStatistics
import drawing.menu.drawDraggableMenu
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import radar.generators.CatGenerator
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.Point2D
import radar.scene.SceneConfig
import java.util.concurrent.*
import kotlin.math.floor
import kotlin.time.measureTime

class TaskThread {
    private val taskQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private val thread: Thread =
        Thread {
            while (true) {
                try {
                    taskQueue.poll(1, TimeUnit.SECONDS)?.run()
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }

    init {
        thread.start()
    }

    fun submitTask(task: Runnable): CompletableFuture<Void> {
        val future = CompletableFuture<Void>()
        val runnable =
            Runnable {
                try {
                    task.run()
                    future.complete(null)
                } catch (e: Exception) {
                    future.completeExceptionally(e)
                }
            }
        taskQueue.put(runnable)
        return future
    }

    fun shutdown() {
        thread.interrupt()
        executor.shutdown()
    }
}

data class CatParticleForDraw(
    val cat: CatParticle,
    var from: Point2D,
    var to: Point2D,
) {
    fun nextStep(progress: Double) {
        from.x += (to.x - from.x) * progress
        from.y += (to.y - from.y) * progress
    }


    fun updateGoal() {
        val newCoords = cat.coordinates.copy()

        // Проверка телепортации по оси X
        if (kotlin.math.abs(newCoords.x - to.x) > GRID_SIZE_X / 2) {
            from.x = newCoords.x // Мгновенный переход
        }
        // Проверка телепортации по оси Y
        if (kotlin.math.abs(newCoords.y - to.y) > GRID_SIZE_Y / 2) {
            from.y = newCoords.y // Мгновенный переход
        }

        // Обновляем конечные координаты
        to = newCoords
    }
}

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
        val modelingState = mutableStateOf(ModelingStates.FINISHED)

        val frameDurationMs = floor(1000.toDouble() / FPS).toInt() // Время одного кадра

        fun calculateStepsCount() = SceneConfig.tau / frameDurationMs
        var steps = calculateStepsCount()
        val step = mutableStateOf(0L)
        val scope = rememberCoroutineScope()
        Window(onCloseRequest = ::exitApplication, title = "Cat Lab UI") {
            var timeModeling by remember { mutableStateOf(0L) }
            var timeUpdating by remember { mutableStateOf(0L) }
            val timeDrawing = mutableStateOf(0L)
            val needToUpdateConfig = mutableStateOf(false)

            LaunchedEffect(SceneConfig.particleCount, state.value, needToUpdateConfig.value) {
                if (state.value != UIStates.DRAWING_IS_FINISHED) {
                    needToUpdateConfig.value = !needToUpdateConfig.value // wait for ending of modeling TODO: is there a better way?
                }
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
                        if (state.value == UIStates.DRAWING_IS_FINISHED && modelingState.value == ModelingStates.FINISHED) {
                            catsToDraw.forEach {
                                it.updateGoal()
                            }
                            modelingState.value = ModelingStates.MODELING
                            state.value = UIStates.READY_TO_DRAW
                            timeModeling =
                                measureTime {
                                    taskThread
                                        .submitTask {
                                            catScene.updateScene()
                                        }.join()
                                }.inWholeMilliseconds
                            modelingState.value = ModelingStates.FINISHED
                        }
                    }
                    delay(1)
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
                    delay(1)
                }
            }
            drawScene(catsToDraw, catScene.sceneConfig, timeDrawing)
            drawDraggableMenu(catScene.sceneConfig)
            drawStatistics(timeModeling, timeUpdating, timeDrawing.value, step.value, catScene.particles)
        }
    }
