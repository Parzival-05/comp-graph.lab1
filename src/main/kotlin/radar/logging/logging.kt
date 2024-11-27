package radar.logging

import CatSimulation.Companion.MAX_INTERACTIONS_DISPLAYED
import radar.scene.CatInteraction
import radar.scene.CatCollision
import java.sql.Timestamp

val GlobalInteractionLog = mutableListOf<CatInteraction>()

fun logging(collision: CatCollision) {
    val time = Timestamp(System.currentTimeMillis())
    val cat1Id = collision.particle1.id
    val cat2Id = collision.particle2.id
    GlobalInteractionLog.add(CatInteraction(time, cat1Id, cat2Id, collision.catState))
    if (GlobalInteractionLog.size > MAX_INTERACTIONS_DISPLAYED) {
        GlobalInteractionLog.removeAt(0)
    }
}
