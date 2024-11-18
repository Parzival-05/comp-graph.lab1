package radar.scene

import java.sql.Timestamp

data class CatInteraction(
    val time: Timestamp,
    val particleId1: Int,
    val particleId2: Int,
    val type: CatStates
)