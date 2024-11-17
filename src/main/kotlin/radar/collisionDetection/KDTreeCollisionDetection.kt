package radar.collisionDetection

import CollisionDetection.Companion.BATCH_SIZE
import org.tinspin.index.PointDistance
import org.tinspin.index.kdtree.KDTree
import radar.metrics.euclidean
import radar.metrics.manhattan
import radar.scene.*


class KDTreeCollisionDetection {
    companion object {
        fun findCollisions(scene: CatScene): Array<CatCollision> {
            val cats = scene.particles
            val kdTree = KDTree.create<CatParticle>(2)
            for (cat in cats) {
                val coordinates = cat.coordinates
                kdTree.insert(doubleArrayOf(coordinates.x.toDouble(), coordinates.y.toDouble()), cat)
            }
            val collisions = mutableSetOf<CatCollision>()
            val pd = PointDistance { p1: DoubleArray, p2: DoubleArray ->
                val distF = when (scene.sceneConfig.metric) {
                    SceneConfig.Companion.MetricType.EUCLIDEAN -> ::euclidean
                    SceneConfig.Companion.MetricType.MANHATTAN -> ::manhattan
                    else -> TODO()
                }
                distF(Point2D(p1[0].toFloat(), p1[1].toFloat()), Point2D(p2[0].toFloat(), p2[1].toFloat())).toDouble()
            }
            for (cat in cats) {
                val coordinates = cat.coordinates
                val (x, y) = Pair(coordinates.x.toDouble(), coordinates.y.toDouble())
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
                        val dist = scene.calcDistance(cat.coordinates, catNeighbour.coordinates)
                        if (dist < scene.sceneConfig.hissDist) {
                            val state = scene.calcNewState(dist)
                            val collision = CatCollision(cat, catNeighbour, dist, state)
                            collisions.add(collision)
                            if (state != CatStates.CALM) {
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