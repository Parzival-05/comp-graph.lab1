package core.base

import core.base.generators.BaseOffsetGenerator


abstract class BaseScene<P : BaseParticle<T, O>, T : BasePoint<O>, O : BaseOffset<T>, C : BaseCollision<P, T, O>, OG : BaseOffsetGenerator<P, T, O>>(
    open var particles: ArrayList<P>
) {
    open fun updateScene(offsetGenerator: OG) {
        this.move(offsetGenerator)
        findAndReactCollisions()
    }

    open fun findAndReactCollisions() {
        val collisions = this.findCollisions()
        this.reactCollisions(collisions)
    }

    open fun move(offsetGenerator: OG) {
        this.particles.forEach {
            val offset = offsetGenerator.generate(it)
            offset.move(it.coordinates)
        }
        particles.removeIf { !it.isInScene() }
    }

    protected abstract fun findCollisions(): Array<C>
    protected abstract fun reactCollisions(collisions: Array<C>)
}
