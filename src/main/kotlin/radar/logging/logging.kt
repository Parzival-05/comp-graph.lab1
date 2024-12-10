package radar.logging

import CatSimulation.Companion.MAX_INTERACTIONS_DISPLAYED
import radar.scene.CatInteraction
import radar.scene.CatParticle
import radar.scene.CatStates
import java.sql.Timestamp

val GlobalInteractionLog = mutableListOf<CatInteraction>()

fun log(cat1: CatParticle, cat2: CatParticle, state: CatStates) {
    val time = Timestamp(System.currentTimeMillis())
    val cat1Id = cat1.id
    val cat2Id = cat2.id
    GlobalInteractionLog.add(CatInteraction(time, cat1Id, cat2Id, state))
    if (GlobalInteractionLog.size > MAX_INTERACTIONS_DISPLAYED) {
        GlobalInteractionLog.removeAt(0)
    }
}
