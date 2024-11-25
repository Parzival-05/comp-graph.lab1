package radar.collisionDetection

import CollisionDetection.Companion.BATCH_SIZE
import org.tinspin.index.PointDistance
import org.tinspin.index.kdtree.KDTree
import radar.scene.*


class KDTreeCollisionDetection {
    companion object {
        fun findCollisions(scene: CatScene): Array<CatCollision> {
            val cats = scene.particles
            val kdTree = KDTree.create<CatParticle>(2)
            for (cat in cats) {
                val coordinates = cat.coordinates
                kdTree.insert(doubleArrayOf(coordinates.x, coordinates.y), cat)
            }
            val collisions = mutableSetOf<CatCollision>()
            val pd = PointDistance { p1: DoubleArray, p2: DoubleArray ->
                scene.sceneConfig.metricFunction(
                    Point2D(p1[0], p1[1]), Point2D(p2[0], p2[1])
                )
            }
            for (cat in cats) {
                val coordinates = cat.coordinates
                val (x, y) = Pair(coordinates.x, coordinates.y)
                var catsAreClose = true
                var batch = 0
                while (catsAreClose) {
                    batch += BATCH_SIZE
                    val nearestCats = kdTree.queryKnn(doubleArrayOf(x, y), 1 + batch, pd)
                    for (i in 1..1 + (batch - BATCH_SIZE)) {
                        if (nearestCats.hasNext()) {
                            nearestCats.next()
                        } else {
                            catsAreClose = false
                            break
                        }
                    }
                    for (neighbourCat in nearestCats) {
                        val catNeighbour = neighbourCat.value()
                        if (catNeighbour == cat) {
                            continue
                        }
                        val dist = neighbourCat.dist()
                        if (dist < scene.sceneConfig.hissDist) {
                            val state = scene.calcNewState(dist)
                            if (state != CatStates.CALM) {
                                val collision = CatCollision(cat, catNeighbour, dist, state)
                                collisions.add(collision)
                                break
                            }
                        } else {
                            catsAreClose = false
                            break
                        }
                    }
                }
            }
            return collisions.toTypedArray()
        }
    }
}