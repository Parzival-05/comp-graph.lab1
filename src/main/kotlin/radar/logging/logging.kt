package radar.logging

import CatSimulation.Companion.MAX_LOGS_DISPLAYED
import radar.scene.CatInteraction
import radar.scene.CatParticle
import radar.scene.CatStateChange
import radar.scene.CatStates
import java.sql.Timestamp

val GlobalInteractionLog = mutableListOf<Loggable>()

fun logInteraction(
    cat1: CatParticle,
    cat2: CatParticle,
    state: CatStates,
) {
    val time = Timestamp(System.currentTimeMillis())
    val cat1Id = cat1.id
    val cat2Id = cat2.id
    GlobalInteractionLog.add(CatInteraction(time, cat1Id, cat2Id, state))
    // todo: bad
    if (GlobalInteractionLog.size > MAX_LOGS_DISPLAYED) {
        GlobalInteractionLog.removeAt(0)
    }
}

fun logStateChange(cat: CatParticle) {
    val time = Timestamp(System.currentTimeMillis())
    GlobalInteractionLog.add(CatStateChange(time, cat.id, cat.state))
    if (GlobalInteractionLog.size > MAX_LOGS_DISPLAYED) {
        GlobalInteractionLog.removeAt(0)
    }
}
