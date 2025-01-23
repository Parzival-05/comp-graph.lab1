package radar.scene

import radar.logging.Loggable
import java.sql.Timestamp

/**
 * Represents cat's state change. If a cat changed state by interacting with other cat,
 * interaction will be recorded as [CatInteraction]
 *
 * @property time The timestamp of the state change.
 * @property catId The ID of the cat.
 * @property type The type of state (e.g., `SLEEPING`, `DEAD`).
 */
data class CatStateChange(
    override val time: Timestamp,
    val catId: Int,
    val type: CatStates,
) : Loggable
