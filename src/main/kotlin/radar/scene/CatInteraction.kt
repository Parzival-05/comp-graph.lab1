package radar.scene

import java.sql.Timestamp

/**
 * Represents an interaction between two `CatParticles` at a specific time.
 *
 * @property time The timestamp of the interaction.
 * @property particleId1 The ID of the first particle.
 * @property particleId2 The ID of the second particle.
 * @property type The type of interaction (e.g., `HISS`, `FIGHT`).
 */
data class CatInteraction(
    val time: Timestamp,
    val particleId1: Int,
    val particleId2: Int,
    val type: CatStates,
)
