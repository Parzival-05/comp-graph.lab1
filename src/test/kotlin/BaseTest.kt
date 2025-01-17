import org.junit.jupiter.api.BeforeEach
import radar.scene.SceneConfig

abstract class BaseTest {
    @BeforeEach
    fun setUp() {
        SceneConfig.loadConfig("test_config.properties")
    }
}
