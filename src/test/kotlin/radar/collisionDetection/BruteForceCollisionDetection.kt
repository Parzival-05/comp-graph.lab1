package radar.collisionDetection

import core.base.BaseCollisionDetection
import radar.generators.MoveGenerator
import radar.scene.CatCollision
import radar.scene.CatParticle
import radar.scene.CatScene
import radar.scene.CatStates
import radar.scene.Offset2D
import radar.scene.Point2D

class BruteForceCollisionDetection : BaseCollisionDetection<CatScene, CatParticle, Point2D, Offset2D, CatCollision, MoveGenerator> {
    override fun findCollisions(scene: CatScene): Array<CatCollision> {
        val cats = scene.particles
        val collisions = mutableListOf<CatCollision>()
        for (i in cats.indices) {
            for (j in i + 1..<cats.size) {
                val cat1 = cats[i]
                val cat2 = cats[j]
                val dist = scene.sceneConfig.metricFunction(cat1.coordinates, cat2.coordinates)
                val state = scene.calcNewState(dist)
                if (state != CatStates.CALM) {
                    collisions.add(CatCollision(cat1, cat2, dist, state))
                }
            }
        }
        return collisions.toTypedArray()
    }
}
