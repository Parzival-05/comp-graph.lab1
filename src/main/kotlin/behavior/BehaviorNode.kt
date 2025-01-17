package behavior

import radar.scene.CatParticle

/**
 * Functional interface for behavior tree node. Method [tick] is a function that accepts [CatParticle] and is called
 * on each frame.
 *
 */
interface BehaviorNode {
    fun tick(cat: CatParticle): BehaviorStatus
}
