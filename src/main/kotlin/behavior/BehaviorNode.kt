package behavior

import radar.scene.CatParticle

abstract class BehaviorNode {
    abstract fun tick(cat: CatParticle): BehaviorStatus
}
