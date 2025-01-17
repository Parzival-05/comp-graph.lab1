package radar.scene.behavior

import radar.scene.CatParticle
import radar.scene.behavior.gang.CatRole

/**
 * Factory class for creating [CatBehaviorManager] instances based on the given [CatRole].
 *
 * @param role The role of the cat.
 * @param cat The cat to create the behavior manager for.
 */
object BehaviorManagerFactory {
    fun create(
        role: CatRole,
        cat: CatParticle,
    ): CatBehaviorManager =
        when (role) {
            CatRole.DEFAULT -> SimpleBehaviorManager(cat)
            CatRole.GHOST -> GhostBehaviorManager(cat)
            CatRole.POSSESSED -> PossessedBehaviorManager(cat)
        }
}
