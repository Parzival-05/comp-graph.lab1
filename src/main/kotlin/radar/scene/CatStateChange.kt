package radar.scene

import radar.logging.Loggable
import java.sql.Timestamp

data class CatStateChange(
    override val time: Timestamp,
    val catId: Int,
    val type: CatStates,
) : Loggable
