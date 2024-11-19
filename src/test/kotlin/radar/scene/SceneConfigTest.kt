package radar.scene

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SceneConfigTest {

    @Test
    fun `test default metric is EUCLIDEAN`() {
        val config = SceneConfig()
        assertEquals(SceneConfig.Companion.MetricType.GREAT_CIRCLE, config.metric)
    }

    @Test
    fun `test hissDist cannot be less than fightDist`() {
        val config = SceneConfig()
        config.fightDist = 2
        val exception = assertThrows<RuntimeException> {
            config.hissDist = 1
        }
        assertEquals("Hiss distance can't be less than fight distance.", exception.message)
    }

    @Test
    fun `test hissDist can be set greater than fightDist`() {
        val config = SceneConfig()
        config.fightDist = 1
        config.hissDist = 2
        assertEquals(2, config.hissDist)
    }
}