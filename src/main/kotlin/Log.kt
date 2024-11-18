import androidx.compose.runtime.mutableStateListOf
import radar.scene.CatCollision
import radar.scene.CatInteraction
import radar.scene.CatStates
import java.sql.Timestamp

val GlobalInteractionLog = mutableStateListOf<CatInteraction>()

fun log(collision: CatCollision, newState: CatStates) {
    val time = Timestamp(System.currentTimeMillis())
    val cat1Id = collision.particle1.id
    val cat2Id = collision.particle2.id
    GlobalInteractionLog.add(CatInteraction(time, cat1Id, cat2Id, newState))
}