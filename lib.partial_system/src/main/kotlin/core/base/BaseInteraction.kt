package core.base

import java.sql.Timestamp

data class BaseInteraction<T>(
    val time: Timestamp,
    val particleId1: Int,
    val particleId2: Int,
    val type: T
)