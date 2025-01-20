// import CatSimulation.Companion.FPS
// import androidx.compose.runtime.*
// import androidx.compose.ui.window.Window
// import androidx.compose.ui.window.application
// import classes.UIStates
// import drawing.drawScene
// import drawing.drawStatistics
// import drawing.menu.drawDraggableMenu
// import kotlinx.coroutines.delay
// import kotlinx.coroutines.launch
// import radar.generators.CatGenerator
// import radar.scene.CatParticle
// import radar.scene.CatScene
// import radar.scene.Point2D
// import radar.scene.SceneConfig
// import java.util.concurrent.*
// import kotlin.math.floor
// import kotlin.time.measureTime
//
// class TaskThread {
//    private val taskQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
//    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
//    private val thread: Thread =
//        Thread {
//            while (true) {
//                try {
//                    taskQueue.poll(1, TimeUnit.SECONDS)?.run()
//                } catch (e: InterruptedException) {
//                    Thread.currentThread().interrupt()
//                }
//            }
//        }
//
//    init {
//        thread.start()
//    }
//
//    fun submitTask(task: Runnable): CompletableFuture<Void> {
//        val future = CompletableFuture<Void>()
//        val runnable =
//            Runnable {
//                try {
//                    task.run()
//                    future.complete(null)
//                } catch (e: Exception) {
//                    future.completeExceptionally(e)
//                }
//            }
//        taskQueue.put(runnable)
//        return future
//    }
//
//    fun shutdown() {
//        thread.interrupt()
//        executor.shutdown()
//    }
// }
//
// fun main() =
//    application {
//        SceneConfig.loadConfig("config.properties")
//        val catGenerator = CatGenerator()
//        var cats by remember { mutableStateOf(ArrayList<CatParticle>()) }
//        var copyCats by remember { mutableStateOf(ArrayList<CatParticle>()) }
//        for (i in 1..SceneConfig.particleCount) {
//            cats.add(catGenerator.generate())
//        }
//        val catScene = CatScene(cats, SceneConfig)
//        val state = mutableStateOf(UIStates.READY_TO_DRAW)
//
//        val frameDurationMs = floor(1000.toDouble() / FPS).toInt() // Время одного кадра
//
//        fun calculateStepsCount() = (SceneConfig.tau / frameDurationMs).toLong()
//        var steps = calculateStepsCount()
//        val step = mutableStateOf(0L)
//        val scope = rememberCoroutineScope()
//        Window(onCloseRequest = ::exitApplication, title = "Cat Lab UI") {
//            var currentCats: Array<CatParticle> by remember { mutableStateOf(emptyArray()) }
//            var timeModeling by remember { mutableStateOf(0L) }
//            var timeUpdating by remember { mutableStateOf(0L) }
//            val timeDrawing = mutableStateOf(0L)
//
//            LaunchedEffect(SceneConfig.particleCount) {
//                while (state.value != UIStates.DRAWING_IS_FINISHED) {
//                    delay(1) // wait for ending of modeling
//                }
//                while (cats.size < SceneConfig.particleCount) {
//                    cats.add(catGenerator.generate())
//                }
//                while (cats.size > SceneConfig.particleCount) {
//                    cats.removeLast()
//                }
//                currentCats = cats.toTypedArray().copyOf()
//            }
//
//            LaunchedEffect(state.value) {
//                if (state.value == UIStates.DRAWING_IS_FINISHED) {
//                    currentCats = copyCats.toTypedArray().copyOf()
//                }
//                //                if (state.value == UIStates.DRAWING) {
//                //                }
//            }
//
//            //            scope.launch {
//            //                var lastState = state.value
//            //                var lastStep = step.value
//            //                while (true) {
//            //                    if (lastState != state.value || lastStep != step.value) {
//            //                        println("${state.value}, ${step.value}")
//            //                        lastState = state.value
//            //                        lastStep = step.value
//            //                    }
//            //                    delay(1)
//            //                }
//            //            }
//            scope.launch {
//                val taskThread = TaskThread()
//                while (true) {
//                    if (!SceneConfig.isOnPause) {
//                        if (state.value == UIStates.DRAWING_IS_FINISHED) {
//                            state.value = UIStates.READY_TO_DRAW
//                            timeModeling =
//                                measureTime {
//                                    taskThread
//                                        .submitTask {
//                                            catScene.updateScene()
//                                            copyCats.clear()
//                                            copyCats.addAll(catScene.particles)
//                                        }.join()
//                                }.inWholeMilliseconds
//                        }
//                    }
//                    delay(1)
//                }
//            }
//            var progress = 0.toDouble()
//            scope.launch {
//                while (true) {
//                    if (!SceneConfig.isOnPause) {
//                        if (state.value == UIStates.READY_TO_DRAW) {
//                            if (step.value < steps) {
//                                timeUpdating =
//                                    measureTime {
//                                        steps = calculateStepsCount()
//                                        progress += (1 - progress) / (steps - step.value)
//                                        //                                        println(progress)
//                                        currentCats.forEach { cat ->
//                                            if (cat.id == 0 && cat.previousCoordinates != cat.coordinates) {
//                                                println(true)
//                                            }
//                                            val lastPreviousCoordinates = cat.previousCoordinates
//                                            cat.previousCoordinates = cat.coordinates
//                                            cat.coordinates =
//                                                Point2D(
//                                                    x = cat.coordinates.x + (cat.coordinates.x - lastPreviousCoordinates.x) * progress,
//                                                    y = cat.coordinates.y + (cat.coordinates.y - lastPreviousCoordinates.y) * progress,
//                                                )
//                                        }
//                                        state.value = UIStates.DRAWING
//                                        step.value += 1
//                                    }.inWholeMilliseconds
//                                delay(frameDurationMs - 1 - timeUpdating - timeDrawing.value)
//                            } else {
//                                state.value = UIStates.DRAWING_IS_FINISHED
//                                step.value = 0
//                                progress = 0.toDouble()
//                            }
//                        }
//                    }
//                    delay(1)
//                }
//            }
//            drawScene(currentCats, state, catScene.sceneConfig, timeDrawing)
//            drawDraggableMenu(catScene.sceneConfig)
//            drawStatistics(timeModeling, timeUpdating, timeDrawing.value, step.value, catScene.particles)
//        }
//    }
