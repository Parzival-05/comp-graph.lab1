package radar.scene

import radar.logging.Loggable
import java.sql.Timestamp

/**
 * Represents an interaction between two [CatParticle] at a specific time.
 *
 * @property time The timestamp of the interaction.
 * @property catId1 The ID of the first cat.
 * @property catId2 The ID of the second cat.
 * @property type The type of interaction (e.g., `HISS`, `FIGHT`).
 */
data class CatInteraction(
    override val time: Timestamp,
    val catId1: Int,
    val catId2: Int,
    val type: CatStates,
) : Loggable
