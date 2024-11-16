import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import drawing.catSimulationScreen
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
    Window(onCloseRequest = ::exitApplication, title = "Cat Lab UI") {
        catSimulationScreen(catScene, moveGenerator)
    }
}