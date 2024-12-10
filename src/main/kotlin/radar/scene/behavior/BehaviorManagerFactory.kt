package radar.scene.behavior

import radar.scene.CatParticle
import radar.scene.behavior.gang.CatRole

object BehaviorManagerFactory {
    fun create(role: CatRole, cat: CatParticle): CatBehaviorManager {
        return when (role) {
            CatRole.DEFAULT -> SimpleBehaviorManager(cat)
            CatRole.GHOST -> GhostBehaviorManager(cat)
            CatRole.POSSESSED -> PossessedBehaviorManager(cat)
        }
    }
}
