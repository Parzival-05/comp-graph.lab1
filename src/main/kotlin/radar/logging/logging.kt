package radar.logging

import CatSimulation.Companion.MAX_LOGS_DISPLAYED
import radar.scene.CatInteraction
import radar.scene.CatParticle
import radar.scene.CatStateChange
import radar.scene.CatStates
import java.sql.Timestamp
import java.time.Clock

object InteractionLogger {
    private val log = mutableListOf<Loggable>()

    private fun addLogEntry(entry: Loggable) {
        log.add(entry)
        if (log.size > MAX_LOGS_DISPLAYED) {
            log.removeFirst()
        }
    }

    fun logInteraction(
        cat1: CatParticle,
        cat2: CatParticle,
        state: CatStates,
        clock: Clock = Clock.systemUTC(),
    ) {
        val time = Timestamp(clock.millis())
        addLogEntry(CatInteraction(time, cat1.id, cat2.id, state))
    }

    fun logStateChange(
        cat: CatParticle,
        clock: Clock = Clock.systemUTC(),
    ) {
        val time = Timestamp(clock.millis())
        addLogEntry(CatStateChange(time, cat.id, cat.state))
    }

    fun getLogs(): List<Loggable> = log.toList()
}
