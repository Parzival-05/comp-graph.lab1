package radar.scene

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import radar.generators.CatGenerator

class CatEmitterTest {
    @Test
    fun `test emit generates correct number of particles`() {
        val generator = CatGenerator()
        val emitter = CatEmitter(particleGenerator = generator)
        val cats = emitter.emit(5)
        assertEquals(5, cats.size)
    }
}
