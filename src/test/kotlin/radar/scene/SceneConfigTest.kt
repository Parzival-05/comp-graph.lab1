package radar.scene

import BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SceneConfigTest : BaseTest() {
    @Test
    fun `test hissDist cannot be less than fightDist`() {
        SceneConfig.fightDist = 2
        val exception =
            assertThrows<RuntimeException> {
                SceneConfig.hissDist = 1
            }
        assertEquals("Hiss distance can't be less than fight distance.", exception.message)
    }

    @Test
    fun `test hissDist can be set greater than fightDist`() {
        SceneConfig.fightDist = 1
        SceneConfig.hissDist = 2
        assertEquals(2, SceneConfig.hissDist)
    }
}
