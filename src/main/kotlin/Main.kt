import CatSimulation.Companion.PARTICLE_COUNT
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import classes.UIStates
import drawing.drawScene
import drawing.updateScene
import kotlinx.coroutines.delay
import radar.generators.CatGenerator
import radar.generators.MoveGenerator
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.SceneConfig


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

        LaunchedEffect(Unit) {
            while (true) {
                if (state.value == UIStates.MODELING) {
                    catScene.updateScene(moveGenerator)
                    state.value = UIStates.UPDATE_DATA
                }
                delay(3)
            }
        }

        updateScene(catScene, state) { updatedCats ->
            currentCats = updatedCats
        }
        drawScene(currentCats, state)
    }
}
